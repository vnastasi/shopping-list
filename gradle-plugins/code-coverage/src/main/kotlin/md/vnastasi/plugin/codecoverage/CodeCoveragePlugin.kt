package md.vnastasi.plugin.codecoverage

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.file.Directory
import org.gradle.api.file.FileTree
import org.gradle.api.provider.Provider
import org.gradle.api.provider.ProviderFactory
import org.gradle.configurationcache.extensions.capitalized
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.register
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.tasks.JacocoReport
import javax.inject.Inject

private const val PROPERTY_TARGET_BUILD_TYPE = "targetBuildType"
private const val DEFAULT_TARGET_BUILD_TYPE = "debug"

@Suppress("unused")
class CodeCoveragePlugin @Inject constructor(
    private val providers: ProviderFactory
) : Plugin<Project> {

    override fun apply(target: Project) {
        val targetBuildType = providers.gradleProperty(PROPERTY_TARGET_BUILD_TYPE).getOrElse(DEFAULT_TARGET_BUILD_TYPE)

        val libs = target.extensions.getByType<VersionCatalogsExtension>().named("libs")

        target.subprojects.forEach { with(libs) { it.enableTestCoverageData(targetBuildType) } }

        target.pluginManager.apply("jacoco")

        target.tasks.register<JacocoReport>("jacocoTestReport") {
            group = "verification"

            dependsOn(target.getAllTestTasks(targetBuildType))

            reports {
                html.apply {
                    required.set(true)
                    outputLocation.set(target.layout.buildDirectory.dir("reports/jacoco/${targetBuildType}"))
                }
                xml.apply {
                    required.set(false)
                }
                csv.apply {
                    required.set(false)
                }
            }

            executionData.setFrom(target.getExecutionDataDirectories(targetBuildType))
            sourceDirectories.setFrom(target.getAllSourceDirectories())
            classDirectories.setFrom(target.getAllClassDirectories(targetBuildType))
        }

        target.extensions.configure<JacocoPluginExtension> {
            toolVersion = libs.findVersion("jacoco").get().requiredVersion
        }
    }

    private fun Project.getAllTestTasks(buildType: String): List<Provider<Task>> = subprojects
        .mapNotNull { it.tasks.findByName("test${buildType.capitalized()}UnitTest") }
        .map { providers.provider { it } }

    private fun Project.getAllSourceDirectories(): List<Provider<Directory>> = subprojects
        .map { it.layout.projectDirectory.dir("src/main/java") }
        .map { providers.provider { it } }

    private fun Project.getAllClassDirectories(buildType: String): List<Provider<Directory>> = subprojects
        .map { it.layout.buildDirectory.dir("tmp/kotlin-classes/${buildType}") }

    private fun Project.getExecutionDataDirectories(buildType: String): List<Provider<FileTree>> = subprojects
        .map { it.layout.buildDirectory.dir("outputs/unit_test_code_coverage/${buildType}UnitTest") }
        .map { provider ->
            provider.map { directory ->
                directory.asFileTree.matching {
                    include { it.name.endsWith(".exec") }
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
                testCoverage {
                    jacocoVersion = findVersion("jacoco").get().requiredVersion
                }
            }
        }
    }
}
