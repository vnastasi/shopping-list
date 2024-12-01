package md.vnastasi.plugin.secrets

import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ValueSource
import org.gradle.api.provider.ValueSourceParameters
import java.util.Properties

internal abstract class LocalPropertiesValueSource : ValueSource<Properties, LocalPropertiesValueSource.Params> {

    override fun obtain(): Properties? =
        Properties().apply { load(parameters.propertiesFile.get().asFile.inputStream()) }

    interface Params : ValueSourceParameters {

        val propertiesFile: RegularFileProperty
    }
}
