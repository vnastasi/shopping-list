package md.vnastasi.plugin.abs.task

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Provider
import org.gradle.api.provider.ProviderFactory
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

private const val CATALOG_KEY_PROJECT_VERSION_NAME = "project-version-name"
private const val CATALOG_KEY_PROJECT_VERSION_CODE = "project-version-code"

private const val PROPERTY_VERSION = "newAppVersion"

abstract class UpdateProjectVersion @Inject constructor(
    private val providers: ProviderFactory
) : DefaultTask() {

    @get:InputFile
    abstract val versionCatalogFile: RegularFileProperty

    private val providedVersion: Provider<String>
        get() = providers.environmentVariable(PROPERTY_VERSION).orElse(providers.gradleProperty(PROPERTY_VERSION))

    @TaskAction
    fun update() {
        val fileLines = versionCatalogFile.get().asFile.readLines()
        val projectVersionName = fileLines.withIndex().first { it.value.startsWith(CATALOG_KEY_PROJECT_VERSION_NAME) }
        val projectVersionCode = fileLines.withIndex().first { it.value.startsWith(CATALOG_KEY_PROJECT_VERSION_CODE) }

        var versionName = projectVersionName.value.replace("\"", "").split(" = ")[1]
        var versionCode = projectVersionCode.value.replace("\"", "").split(" = ")[1].toInt()

        val newVersion = providedVersion.orNull
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
    }
}
