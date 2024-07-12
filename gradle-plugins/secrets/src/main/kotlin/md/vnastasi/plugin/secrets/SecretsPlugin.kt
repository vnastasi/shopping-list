package md.vnastasi.plugin.secrets

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.ProjectLayout
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Provider
import org.gradle.api.provider.ProviderFactory
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.of
import org.gradle.kotlin.dsl.property
import java.util.Properties
import javax.inject.Inject

class SecretsPlugin @Inject constructor(
    private val providers: ProviderFactory,
    private val objects: ObjectFactory
) : Plugin<Project> {

    override fun apply(target: Project) {
        val localProperties = providers.of(LocalPropertiesValueSource::class) {
            parameters.propertiesFile.set(target.rootProject.layout.projectDirectory.file("local.properties"))
        }
        target.extensions.create<SecretsExtension>("secrets", createKeystore(target.rootProject.layout, localProperties))
    }

    private fun createKeystore(projectLayout: ProjectLayout, localProperties: Provider<Properties>): Keystore =
        Keystore(
            file = objects.fileProperty().apply { set(projectLayout.projectDirectory.file(localProperties.property(Keystore.PROP_KEYSTORE_FILE))) },
            storePassword = objects.property<String>().apply { set(localProperties.property(Keystore.PROP_KEYSTORE_PASSWORD)) },
            keyAlias = objects.property<String>().apply { set(localProperties.property(Keystore.PROP_KEYSTORE_KEY_ALIAS)) },
            keyPassword = objects.property<String>().apply { set(localProperties.property(Keystore.PROP_KEYSTORE_KEY_PASSWORD)) }
        )

    private fun Provider<Properties>.property(key: String): Provider<String> =
        providers.environmentVariable(key).orElse(map { it.getProperty(key) })
}
