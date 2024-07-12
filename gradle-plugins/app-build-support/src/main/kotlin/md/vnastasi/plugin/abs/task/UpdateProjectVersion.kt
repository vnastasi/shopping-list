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

abstract class UpdateProjectVersion @Inject constructor(
    private val providers: ProviderFactory,
    private val execOperations: ExecOperations
) : DefaultTask() {

    @get:InputFile
    abstract val versionCatalogFile: RegularFileProperty

    @TaskAction
    fun update() {
        val currentBranch = execOperations.execWithReturn { commandLine("git", "rev-parse", "--abbrev-ref", "HEAD") }
        if (currentBranch != "master") {
            throw GradleException("Not on master branch!")
        }

        val versionName = updateAndGetNewVersion()

        execOperations.exec { commandLine("git", "add", versionCatalogFile.get().asFile.path) }
        execOperations.exec { commandLine("git", "commit", "-m", "Prepare release v${versionName}") }
        execOperations.exec { commandLine("git", "push", "origin", "master") }
    }

    private fun updateAndGetNewVersion(): String {
        val fileLines = versionCatalogFile.get().asFile.readLines()
        val projectVersionName = fileLines.withIndex().first { it.value.startsWith("project-version-name") }
        val projectVersionCode = fileLines.withIndex().first { it.value.startsWith("project-version-code") }

        var versionName = projectVersionName.value.replace("\"", "").split(" = ")[1]
        var versionCode = projectVersionCode.value.replace("\"", "").split(" = ")[1].toInt()

        val newVersion = providers.gradleProperty("version").orNull
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
            set(projectVersionName.index, "project-version-name = \"$versionName\"")
            set(projectVersionCode.index, "project-version-code = \"$versionCode\"")
        }

        versionCatalogFile.get().asFile.writeText(newFileLines.joinToString(separator = "\n"))

        return versionName
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
