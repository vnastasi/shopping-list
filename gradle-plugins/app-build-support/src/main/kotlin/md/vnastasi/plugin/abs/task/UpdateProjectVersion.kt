package md.vnastasi.plugin.abs.task

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ProviderFactory
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

abstract class UpdateProjectVersion @Inject constructor(
    private val providers: ProviderFactory
) : DefaultTask() {

    @get:InputFile
    abstract val versionCatalogFile: RegularFileProperty

    @TaskAction
    fun update() {
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
    }
}
