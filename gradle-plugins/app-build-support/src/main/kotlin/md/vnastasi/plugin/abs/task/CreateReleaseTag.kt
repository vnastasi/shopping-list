package md.vnastasi.plugin.abs.task

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecOperations
import org.gradle.process.ExecSpec
import javax.inject.Inject

abstract class CreateReleaseTag @Inject constructor(
    private val execOperations: ExecOperations
) : DefaultTask() {

    @get:InputFile
    abstract val versionCatalogFile: RegularFileProperty

    @TaskAction
    fun create() {
        val version = versionCatalogFile.get().asFile.readLines().first { it.startsWith("project-version-name") }.split(" = ")[1].replace("\"", "")

        execOperations.execRequired { commandLine("git", "tag", "v$version") }
        execOperations.execRequired { commandLine("git", "push", "origin", "tag", "v$version") }
    }

    private fun ExecOperations.execRequired(block: ExecSpec.() -> Unit) {
        val exitResult = exec(block).exitValue
        require(exitResult == 0)
    }
}
