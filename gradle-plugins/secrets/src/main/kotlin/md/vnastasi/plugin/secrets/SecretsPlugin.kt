package md.vnastasi.plugin.secrets

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.api.provider.ProviderFactory
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.of
import java.util.Properties
import javax.inject.Inject

class SecretsPlugin @Inject constructor(
    private val providers: ProviderFactory
) : Plugin<Project> {

    override fun apply(target: Project) {
        val localProperties = providers.of(LocalPropertiesValueSource::class) {
            parameters.propertiesFile.set(target.rootProject.layout.projectDirectory.file("local.properties"))
        }
        target.extensions.create<SecretsExtension>("secrets").apply {
            keystore {
                file.set(target.rootProject.layout.projectDirectory.file(localProperties.property(Keystore.PROP_KEYSTORE_FILE)))
                storePassword.set(localProperties.property(Keystore.PROP_KEYSTORE_PASSWORD))
                keyAlias.set(localProperties.property(Keystore.PROP_KEYSTORE_KEY_ALIAS))
                keyPassword.set(localProperties.property(Keystore.PROP_KEYSTORE_KEY_PASSWORD))
            }
        }
    }

    private fun Provider<Properties>.property(key: String): Provider<String> {
        var value = providers.environmentVariable(key).getOrElse("")
        if (value.isEmpty()) {
            value = this.map { it.getProperty(key) }.getOrElse("")
        }
        if (value.isEmpty()) {
            throw GradleException("No value found for key <$key>")
        }
        return providers.provider { value }
    }
}
