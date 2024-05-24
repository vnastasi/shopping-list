package md.vnastasi.plugin.detektaggregator

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.register
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.tasks.JacocoReport

@Suppress("unused")
class JacocoAggregatorPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        val libs = target.extensions.getByType<VersionCatalogsExtension>().named("libs")

        target.subprojects.forEach { subproject ->
            subproject.pluginManager.withPlugin(libs.findPlugin("android-library").get().get().pluginId) {
                subproject.extensions.configure<LibraryExtension> {
                    buildTypes {
                        getByName("debug") {
                            enableUnitTestCoverage = true
                            testCoverage {
                                jacocoVersion = libs.findVersion("jacoco").get().requiredVersion
                            }
                        }
                    }
                }
            }
        }

        val sourceDirectories = target.subprojects.map { it.layout.projectDirectory.dir("src/main/java") }
        val classDirectories = target.subprojects.map { it.layout.buildDirectory.dir("tmp/kotlin-classes/debug") }
        val executionDataDirectories = target.subprojects
            .map { subproject ->
                subproject.layout.buildDirectory.dir("outputs/unit_test_code_coverage/debugUnitTest")
            }.map { directoryProvider ->
                directoryProvider.map { directory ->
                    directory.asFileTree.matching { this.include { it.name.endsWith(".exec") } }
                }
            }

        target.pluginManager.apply("jacoco")
        target.tasks.register<JacocoReport>("jacocoTestReport") {
            group = "verification"
            dependsOn(target.subprojects.mapNotNull { it.tasks.findByName("testDebugUnitTest") }.map { target.provider { it } })
            reports {
                html.apply {
                    required.set(true)
                }
                xml.apply {
                    required.set(false)
                }
                csv.apply {
                    required.set(false)
                }
            }
            this.executionData.setFrom(executionDataDirectories)
            this.sourceDirectories.setFrom(sourceDirectories.map { target.provider { it } })
            this.classDirectories.setFrom(classDirectories)
        }

        target.extensions.configure<JacocoPluginExtension> {
            toolVersion = libs.findVersion("jacoco").get().requiredVersion
        }
    }
}