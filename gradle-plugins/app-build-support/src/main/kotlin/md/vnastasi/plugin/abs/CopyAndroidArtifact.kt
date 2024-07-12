package md.vnastasi.plugin.abs

import com.android.build.api.variant.BuiltArtifactsLoader
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileSystemOperations
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.nio.file.Paths
import javax.inject.Inject
import kotlin.io.path.name

abstract class CopyAndroidArtifact @Inject constructor(
    private val fileSystemOperations: FileSystemOperations
) : DefaultTask() {

    @get:InputDirectory
    abstract val artifactDirectory: DirectoryProperty

    @get:Input
    abstract val fileName: Property<String>

    @get:Internal
    abstract val artifactLoader: Property<BuiltArtifactsLoader>

    @get:OutputDirectory
    abstract val targetDirectory: DirectoryProperty

    @TaskAction
    fun copy() {
        val artifacts = artifactLoader.get().load(artifactDirectory.get()) ?: throw GradleException("No built artifacts found")
        artifacts.elements.forEach { artifact ->
            val artifactPath = Paths.get(artifact.outputFile)
            fileSystemOperations.copy {
                from(artifactPath)
                into(targetDirectory)
                rename(artifactPath.fileName.name, fileName.get())
            }
        }
    }
}
