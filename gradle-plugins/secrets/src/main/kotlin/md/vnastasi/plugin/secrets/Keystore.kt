package md.vnastasi.plugin.secrets

import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property

data class Keystore(
    val file: RegularFileProperty,
    val storePassword: Property<String>,
    val keyAlias: Property<String>,
    val keyPassword: Property<String>
) {

    internal companion object {

        const val PROP_KEYSTORE_FILE = "KEYSTORE_FILE"
        const val PROP_KEYSTORE_PASSWORD = "KEYSTORE_PASSWORD"
        const val PROP_KEYSTORE_KEY_ALIAS = "KEYSTORE_KEY_ALIAS"
        const val PROP_KEYSTORE_KEY_PASSWORD = "KEYSTORE_KEY_PASSWORD"
    }
}
