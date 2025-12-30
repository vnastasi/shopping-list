package md.vnastasi.plugin.codecoverage

import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileSystemOperations
import org.gradle.api.file.RelativePath
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.util.UUID
import javax.inject.Inject

abstract class CopyExecData @Inject constructor(
    private val fileSystemOperations: FileSystemOperations
) : DefaultTask() {

    @get:Optional
    @get:Input
    abstract val profile: Property<String>

    @get:InputFiles
    abstract val execDataLocation: ConfigurableFileCollection

    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    @TaskAction
    fun copyExecData() {
        val localOutputDirectory = if (profile.isPresent) {
            outputDirectory.dir(profile.get())
        } else {
            outputDirectory
        }

        execDataLocation.forEach { directory ->
            fileSystemOperations.copy {
                from(directory)
                into(localOutputDirectory)
                eachFile {
                    relativePath = RelativePath(true, name)
                }
                rename { fileName ->
                    randomUuid() + fileName.substring(fileName.lastIndexOf("."))
                }
                includeEmptyDirs = false
            }
        }
    }

    private fun randomUuid() = UUID.randomUUID().toString()
}
