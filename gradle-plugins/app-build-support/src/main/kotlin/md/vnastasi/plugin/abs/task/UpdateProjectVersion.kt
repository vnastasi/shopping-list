package md.vnastasi.plugin.abs.task

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ProviderFactory
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecOperations
import org.gradle.process.ExecSpec
import java.io.ByteArrayOutputStream
import javax.inject.Inject

private const val CATALOG_KEY_PROJECT_VERSION_NAME = "project-version-name"
private const val CATALOG_KEY_PROJECT_VERSION_CODE = "project-version-code"

private const val TASK_PROPERTY_VERSION = "version"

private const val TARGET_BRANCH = "master"

abstract class UpdateProjectVersion @Inject constructor(
    private val providers: ProviderFactory,
    private val execOperations: ExecOperations
) : DefaultTask() {

    @get:InputFile
    abstract val versionCatalogFile: RegularFileProperty

    @TaskAction
    fun update() {
        checkForMasterBranch()
        val versionName = updateAndGetNewVersion()
        commitChanges(versionName)
    }

    private fun checkForMasterBranch() {
        val currentBranch = execOperations.execWithReturn { commandLine("git", "rev-parse", "--abbrev-ref", "HEAD") }
        if (currentBranch != TARGET_BRANCH) {
            throw GradleException("Not on $TARGET_BRANCH branch!")
        }
    }

    private fun updateAndGetNewVersion(): String {
        val fileLines = versionCatalogFile.get().asFile.readLines()
        val projectVersionName = fileLines.withIndex().first { it.value.startsWith(CATALOG_KEY_PROJECT_VERSION_NAME) }
        val projectVersionCode = fileLines.withIndex().first { it.value.startsWith(CATALOG_KEY_PROJECT_VERSION_CODE) }

        var versionName = projectVersionName.value.replace("\"", "").split(" = ")[1]
        var versionCode = projectVersionCode.value.replace("\"", "").split(" = ")[1].toInt()

        val newVersion = providers.gradleProperty(TASK_PROPERTY_VERSION).orNull
        if (newVersion != null) {
            versionName = newVersion
        } else {
            val tokens = versionName.split(".")
            versionName = buildString {
                append(tokens[0])
                append(".")
                append(tokens[1])
                append(".")
                append(tokens[2].toInt() + 1)
            }
        }
        versionCode += 1

        val newFileLines = buildList {
            addAll(fileLines)
            set(projectVersionName.index, "$CATALOG_KEY_PROJECT_VERSION_NAME = \"$versionName\"")
            set(projectVersionCode.index, "$CATALOG_KEY_PROJECT_VERSION_CODE = \"$versionCode\"")
        }

        versionCatalogFile.get().asFile.writeText(newFileLines.joinToString(separator = "\n"))

        return versionName
    }

    private fun commitChanges(versionName: String) {
        execOperations.exec { commandLine("git", "add", versionCatalogFile.get().asFile.path) }
        execOperations.exec { commandLine("git", "commit", "-m", "Prepare release v${versionName}") }
        execOperations.exec { commandLine("git", "push", "origin", TARGET_BRANCH) }
    }

    private fun ExecOperations.execWithReturn(block: ExecSpec.() -> Unit): String =
        ByteArrayOutputStream().use { stream ->
            exec {
                block.invoke(this)
                standardOutput = stream
            }
            stream.toString(Charsets.UTF_8).trim()
        }
}
