package md.vnastasi.plugin.codecoverage

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.LibraryExtension
import md.vnastasi.plugin.support.libs
import md.vnastasi.plugin.support.withPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.file.FileTree
import org.gradle.api.provider.Provider
import org.gradle.api.provider.ProviderFactory
import org.gradle.configurationcache.extensions.capitalized
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.tasks.JacocoCoverageVerification
import org.gradle.testing.jacoco.tasks.JacocoReport
import javax.inject.Inject

private const val EXTENSION_NAME = "codeCoverage"
private const val TASK_GROUP = "verification"
private const val COVERAGE_REPORT_TASK_NAME = "createCodeCoverageReport"
private const val COVERAGE_VERIFICATION_TASK_NAME = "verifyCodeCoverage"

@Suppress("unused")
class CodeCoveragePlugin @Inject constructor(
    private val providers: ProviderFactory
) : Plugin<Project> {

    override fun apply(target: Project): Unit = with(target) {
        val codeCoverageExtension = extensions.create<CodeCoverageExtension>(EXTENSION_NAME)

        pluginManager.apply("jacoco")

        extensions.configure<JacocoPluginExtension> {
            toolVersion = libs.versions.jacoco.get()
        }

        afterEvaluate {
            val targetBuildType = codeCoverageExtension.targetBuildType.get()

            subprojects.forEach { subProject ->
                subProject.pluginManager.withPlugin(libs.plugins.android.library) {
                    subProject.extensions.configure<LibraryExtension> { enableTestCoverageData(targetBuildType, libs.versions.jacoco.get()) }
                }

                subProject.pluginManager.withPlugin(libs.plugins.android.application) {
                    subProject.extensions.configure<ApplicationExtension> { enableTestCoverageData(targetBuildType, libs.versions.jacoco.get()) }
                }
            }

            val executionDataDirectories = getExecDataDirs(targetBuildType)
            val allSourceDirectories = getAllSourceDirs()
            val allClassDirectories = getAllClassDirs(targetBuildType)

            val coverageReportTask = tasks.register<JacocoReport>(COVERAGE_REPORT_TASK_NAME) {
                group = TASK_GROUP
                dependsOn(subprojects.mapNotNull { it.tasks.findByName("test${targetBuildType.capitalized()}UnitTest") }.map { providers.provider { it } })

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

            tasks.register<JacocoCoverageVerification>(COVERAGE_VERIFICATION_TASK_NAME) {
                group = TASK_GROUP
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

    private fun CommonExtension<*, *, *, *, *, *>.enableTestCoverageData(buildType: String, jacocoAgentVersion: String) {
        buildTypes {
            getByName(buildType) {
                enableUnitTestCoverage = true
                enableAndroidTestCoverage = true
            }
        }

        testCoverage {
            jacocoVersion = jacocoAgentVersion
        }
    }
}
