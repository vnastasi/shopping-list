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
            toolVersion = target.libs.versions.jacoco.get()
        }

        target.afterEvaluate {
            with(codeCoverageExtension) {

                subprojects.forEach { subProject ->
                    subProject.pluginManager.withPlugin(libs.plugins.android.library) {
                        subProject.extensions.configure<LibraryExtension> { enableTestCoverageData() }
                    }

                    subProject.pluginManager.withPlugin(libs.plugins.android.application) {
                        subProject.extensions.configure<ApplicationExtension> { enableTestCoverageData() }
                    }
                }

                val executionDataDirectories = getExecDataDirs()
                val allSourceDirectories = getAllSourceDirs() + getAllGeneratedSourceDirs()
                val allClassDirectories = getAllClassDirs()

                tasks.register<CopyExecData>(COPY_UNIT_TEST_EXEC_DATA) {
                    group = TASK_GROUP
                    description = "Copy execution data for unit tests"

                    execDataLocation.setFrom(subprojects.mapNotNull { it.layout.buildDirectory.dir("outputs/unit_test_code_coverage") })

                    outputDirectory.set(rootProject.layout.buildDirectory.dir("exec-data/unit-tests"))
                }

                tasks.register<CopyExecData>(COPY_INSTRUMENTED_TEST_EXEC_DATA) {
                    group = TASK_GROUP
                    description = "Copy execution data for Android instrumented tests"

                    profile.set(providers.environmentVariable(PROPERTY_AVD_PROFILE))

                    execDataLocation.setFrom(project(":app").layout.buildDirectory.dir("outputs/code_coverage"))

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

    context(targetProject: Project, extension: CodeCoverageExtension)
    private fun getAllSourceDirs(): List<Provider<Directory>> = targetProject.subprojects
        .filter { project -> extension.excludedModules.get().none { it.path == project.path } }
        .map { project -> project.layout.projectDirectory.dir("src/main/java") }
        .map { directory -> providers.provider { directory } }

    context(targetProject: Project, extension: CodeCoverageExtension)
    private fun getAllGeneratedSourceDirs(): List<Provider<Directory>> = targetProject.subprojects
        .filter { project -> extension.excludedModules.get().none { it.path == project.path } }
        .map { project -> project.layout.buildDirectory.dir("generated/ksp/${extension.targetBuildType.get()}/kotlin") }

    context(targetProject: Project, extension: CodeCoverageExtension)
    private fun getAllClassDirs(): List<Provider<FileTree>> = targetProject.subprojects
        .filter { project -> extension.excludedModules.get().none { it.path == project.path } }
        .map { project ->
            project.layout.buildDirectory.dir("tmp/kotlin-classes/${extension.targetBuildType.get()}").map { directory ->
                directory.asFileTree.matching {
                    exclude(extension.excludedClasses.get())
                }
            }
        }

    context(targetProject: Project)
    private fun getExecDataDirs() = targetProject.layout.buildDirectory.dir("exec-data")
        .map { directory ->
            directory.asFileTree.matching {
                include("**/*.exec")
                include("**/*.ec")
            }
        }

    context(targetProject: Project, extension: CodeCoverageExtension)
    private fun CommonExtension<*, *, *, *, *, *>.enableTestCoverageData() {
        buildTypes {
            getByName(extension.targetBuildType.get()) {
                enableUnitTestCoverage = true
                enableAndroidTestCoverage = true
            }
        }

        testCoverage {
            jacocoVersion = targetProject.libs.versions.jacoco.get()
        }
    }
}
