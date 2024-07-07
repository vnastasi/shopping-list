package md.vnastasi.plugin.codecoverage

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.file.Directory
import org.gradle.api.file.FileTree
import org.gradle.api.provider.Provider
import org.gradle.api.provider.ProviderFactory
import org.gradle.configurationcache.extensions.capitalized
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.register
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.tasks.JacocoCoverageVerification
import org.gradle.testing.jacoco.tasks.JacocoReport
import javax.inject.Inject

@Suppress("unused")
class CodeCoveragePlugin @Inject constructor(
    private val providers: ProviderFactory
) : Plugin<Project> {

    override fun apply(target: Project) {
        val codeCoverageExtension = target.extensions.create<CodeCoverageExtension>("codeCoverage")

        val libs = target.extensions.getByType<VersionCatalogsExtension>().named("libs")

        target.pluginManager.apply("jacoco")

        target.extensions.configure<JacocoPluginExtension> {
            toolVersion = libs.findVersion("jacoco").get().requiredVersion
        }

        target.afterEvaluate {
            val targetBuildType = codeCoverageExtension.targetBuildType.get()

            target.subprojects.forEach { with(libs) { it.enableTestCoverageData(targetBuildType) } }

            val executionDataDirectories = target.getExecDataDirs(targetBuildType)
            val allSourceDirectories = target.getAllSourceDirs()
            val allClassDirectories = target.getAllClassDirs(targetBuildType)

            val coverageReportTask = target.tasks.register<JacocoReport>("createCodeCoverageReport") {
                group = "verification"
                dependsOn(target.subprojects.mapNotNull { it.tasks.findByName("test${targetBuildType.capitalized()}UnitTest") }.map { providers.provider { it } })

                executionData.setFrom(executionDataDirectories)
                sourceDirectories.setFrom(allSourceDirectories)
                classDirectories.setFrom(allClassDirectories)

                reports {
                    html.apply {
                        required.set(true)
                        outputLocation.set(codeCoverageExtension.reportDirectory)
                    }
                    xml.apply {
                        required.set(false)
                    }
                    csv.apply {
                        required.set(false)
                    }
                }
            }

            target.tasks.register<JacocoCoverageVerification>("verifyCodeCoverage") {
                group = "verification"
                dependsOn(coverageReportTask)

                executionData.setFrom(executionDataDirectories)
                sourceDirectories.setFrom(allSourceDirectories)
                classDirectories.setFrom(allClassDirectories)

                violationRules {
                    rule {
                        limit {
                            minimum = codeCoverageExtension.coverageThreshold.map { it.toBigDecimal() }.get()
                        }
                    }
                }
            }
        }
    }

    private fun Project.getAllSourceDirs(): List<Provider<Directory>> = subprojects
        .map { it.layout.projectDirectory.dir("src/main/java") }
        .map { providers.provider { it } }

    private fun Project.getAllClassDirs(buildType: String): List<Provider<Directory>> = subprojects
        .map { it.layout.buildDirectory.dir("tmp/kotlin-classes/${buildType}") }

    private fun Project.getExecDataDirs(buildType: String): List<Provider<FileTree>> = buildList {
        addAll(getUniTestExecDataDirs(buildType))
        addAll(getInstrumentationTestExecDataDirs(buildType))
    }

    private fun Project.getUniTestExecDataDirs(buildType: String): List<Provider<FileTree>> = subprojects
        .map { it.layout.buildDirectory.dir("outputs/unit_test_code_coverage/${buildType}UnitTest") }
        .map { provider ->
            provider.map { directory ->
                directory.asFileTree.matching {
                    include("*.exec")
                }
            }
        }

    private fun Project.getInstrumentationTestExecDataDirs(buildType: String): List<Provider<FileTree>> = subprojects
        .map { it.layout.buildDirectory.dir("outputs/code_coverage/${buildType}AndroidTest") }
        .map { provider ->
            provider.map { directory ->
                directory.asFileTree.matching {
                    include("**/*.ec")
                }
            }
        }

    context(VersionCatalog)
    private fun Project.enableTestCoverageData(buildType: String) {
        pluginManager.withPlugin(findPlugin("android-library").get().get().pluginId) {
            extensions.configure<LibraryExtension> { enableTestCoverageData(buildType) }
        }

        pluginManager.withPlugin(findPlugin("android-application").get().get().pluginId) {
            extensions.configure<ApplicationExtension> { enableTestCoverageData(buildType) }
        }
    }

    context(VersionCatalog)
    private fun CommonExtension<*, *, *, *, *, *>.enableTestCoverageData(buildType: String) {
        buildTypes {
            getByName(buildType) {
                enableUnitTestCoverage = true
                enableAndroidTestCoverage = true
            }
        }

        testCoverage {
            jacocoVersion = findVersion("jacoco").get().requiredVersion
        }
    }
}
