package md.vnastasi.buildtools

import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.process.CommandLineArgumentProvider
import java.io.File
import java.nio.file.Files

internal class RoomSchemaArgProvider(
    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    val schemaDir: File
) : CommandLineArgumentProvider {

    override fun asArguments(): Iterable<String> {
        val path = schemaDir.toPath()
        if (Files.notExists(path)) {
            Files.createDirectory(path)
        }
        return listOf("room.schemaLocation=${path}")
    }
}
