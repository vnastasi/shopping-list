package md.vnastasi.plugin.abs.task

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecOperations
import javax.inject.Inject

abstract class CreateReleaseTag @Inject constructor(
    private val execOperations: ExecOperations
) : DefaultTask() {

    @get:InputFile
    abstract val versionCatalogFile: RegularFileProperty

    @TaskAction
    fun create() {
        val versionName = getVersionName()

        execOperations.exec { commandLine("git", "tag", "v$versionName") }
        execOperations.exec { commandLine("git", "push", "origin", "tag", "v$versionName") }
    }

    private fun getVersionName(): String =
        versionCatalogFile.get().asFile.readLines()
            .first { it.startsWith("project-version-name") }
            .split(" = ")[1].replace("\"", "")
}
