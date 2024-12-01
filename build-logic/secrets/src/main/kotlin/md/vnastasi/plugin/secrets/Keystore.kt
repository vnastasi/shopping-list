package md.vnastasi.plugin.secrets

import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.property
import javax.inject.Inject

abstract class Keystore @Inject constructor(
    objects: ObjectFactory
) {

    val file: RegularFileProperty = objects.fileProperty()

    val storePassword: Property<String> = objects.property<String>()

    val keyAlias: Property<String> = objects.property<String>()

    val keyPassword: Property<String> = objects.property<String>()

    internal companion object {

        const val PROP_KEYSTORE_FILE = "KEYSTORE_FILE"
        const val PROP_KEYSTORE_PASSWORD = "KEYSTORE_PASSWORD"
        const val PROP_KEYSTORE_KEY_ALIAS = "KEYSTORE_KEY_ALIAS"
        const val PROP_KEYSTORE_KEY_PASSWORD = "KEYSTORE_KEY_PASSWORD"
    }
}
