package md.vnastasi.plugin.codecoverage

import md.vnastasi.plugin.support.libs
import md.vnastasi.plugin.support.requireVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.file.FileTree
import org.gradle.api.provider.Provider
import org.gradle.api.provider.ProviderFactory
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
private const val COPY_UNIT_TEST_EXEC_DATA = "copyUnitTestExecData"
private const val COPY_SCREENSHOT_TEST_EXEC_DATA = "copyScreenshotTestExecData"
private const val COPY_INSTRUMENTED_TEST_EXEC_DATA = "copyInstrumentedTestExecData"

private const val PROPERTY_AVD_PROFILE = "AVD_PROFILE"

@Suppress("unused")
class CodeCoveragePlugin @Inject constructor(
    private val providers: ProviderFactory
) : Plugin<Project> {

    override fun apply(target: Project) {
        val codeCoverageExtension = target.extensions.create<CodeCoverageExtension>(EXTENSION_NAME)

        target.pluginManager.apply("jacoco")

        target.extensions.configure<JacocoPluginExtension> {
            toolVersion = target.libs.requireVersion("jacoco")
        }

        target.afterEvaluate {
            with(codeCoverageExtension) {
                val executionDataDirectories = getExecDataDirs(target)
                val allSourceDirectories = getAllSourceDirs(target, this) + getAllGeneratedSourceDirs(target, this)
                val allClassDirectories = getAllClassDirs(target, this)

                tasks.register<CopyExecData>(COPY_UNIT_TEST_EXEC_DATA) {
                    group = TASK_GROUP
                    description = "Copy execution data for unit tests"

                    execDataLocation.setFrom(
                        subprojects.map { subProject ->
                            target.layout.projectDirectory.dir("${subProject.projectDir.absolutePath}/build/outputs/unit_test_code_coverage")
                        }
                    )

                    outputDirectory.set(rootProject.layout.buildDirectory.dir("exec-data/unit-tests"))
                }

                tasks.register<CopyExecData>(COPY_SCREENSHOT_TEST_EXEC_DATA) {
                    group = TASK_GROUP
                    description = "Copy execution data for screenshot tests"

                    execDataLocation.setFrom(
                        subprojects
                            .map { subProject ->
                                target.layout.projectDirectory.dir("${subProject.projectDir.absolutePath}/build/jacoco")
                            }
                    )

                    outputDirectory.set(rootProject.layout.buildDirectory.dir("exec-data/screenshot-tests"))
                }

                tasks.register<CopyExecData>(COPY_INSTRUMENTED_TEST_EXEC_DATA) {
                    group = TASK_GROUP
                    description = "Copy execution data for Android instrumented tests"

                    profile.set(providers.environmentVariable(PROPERTY_AVD_PROFILE))

                    execDataLocation.setFrom(rootProject.layout.projectDirectory.dir("app/build/outputs/code_coverage"))

                    outputDirectory.set(rootProject.layout.buildDirectory.dir("exec-data/instrumented-tests"))
                }

                val coverageReportTask = tasks.register<JacocoReport>(COVERAGE_REPORT_TASK_NAME) {
                    group = TASK_GROUP

                    executionData.setFrom(executionDataDirectories)
                    sourceDirectories.setFrom(allSourceDirectories)
                    classDirectories.setFrom(allClassDirectories)

                    reports {
                        html.apply {
                            required.set(true)
                            outputLocation.set(codeCoverageExtension.reportDirectory)
                        }
                        xml.apply {
                            required.set(true)
                            outputLocation.set(codeCoverageExtension.reportDirectory.file("jacoco.xml"))
                        }
                        csv.apply {
                            required.set(true)
                            outputLocation.set(codeCoverageExtension.reportDirectory.file("jacoco.csv"))
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
                                minimum = codeCoverageExtension.coverageThreshold.map { it.divide(100.toBigDecimal()) }.get()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getAllSourceDirs(targetProject: Project, extension: CodeCoverageExtension): List<Provider<Directory>> = targetProject.subprojects
        .map { project -> targetProject.layout.projectDirectory.dir("${project.projectDir.absolutePath}/src/main/java") }
        .map { directory -> targetProject.providers.provider { directory } }

    private fun getAllGeneratedSourceDirs(targetProject: Project, extension: CodeCoverageExtension): List<Provider<Directory>> = targetProject.subprojects
        .map { project -> targetProject.layout.projectDirectory.dir("${project.projectDir.absolutePath}/build/generated/ksp/${extension.targetBuildType.get()}/kotlin") }
        .map { directory -> targetProject.providers.provider { directory } }

    private fun getAllClassDirs(targetProject: Project, extension: CodeCoverageExtension): List<Provider<FileTree>> = targetProject.subprojects
        .map { project ->
            targetProject.layout.projectDirectory.dir("${project.projectDir.absolutePath}/build/intermediates/built_in_kotlinc/${extension.targetBuildType.get()}")
        }
        .map { directory ->
            targetProject.providers.provider { directory }.map { dir ->
                dir.asFileTree.matching {
                    exclude(extension.excludedClasses.get())
                }
            }
        }

    private fun getExecDataDirs(targetProject: Project) = targetProject.layout.buildDirectory.dir("exec-data")
        .map { directory ->
            directory.asFileTree.matching {
                include("**/*.exec")
                include("**/*.ec")
            }
        }
}
