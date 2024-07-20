package md.vnastasi.plugin.secrets

import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory
import javax.inject.Inject

abstract class SecretsExtension @Inject constructor(
    objects: ObjectFactory
) {

    val keystore: Keystore = objects.newInstance(Keystore::class.java)

    fun keystore(action: Action<Keystore>) {
        action.execute(keystore)
    }
}
