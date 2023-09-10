import md.vnastasi.buildtools.RoomSchemaArgProvider
import org.gradle.process.CommandLineArgumentProvider
import java.io.File

fun roomSchemaDir(schemaDir: File): CommandLineArgumentProvider = RoomSchemaArgProvider(schemaDir)
