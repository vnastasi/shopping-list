package md.vnastasi.plugin.conventions

import org.gradle.api.plugins.AppliedPlugin
import org.gradle.api.plugins.PluginManager
import org.gradle.api.provider.Provider
import org.gradle.plugin.use.PluginDependency

internal fun PluginManager.apply(plugin: Provider<PluginDependency>) {
    apply(plugin.get().pluginId)
}

internal fun PluginManager.applyAndConfigure(plugin: Provider<PluginDependency>, configuration: AppliedPlugin.() -> Unit) {
    val pluginId = plugin.get().pluginId
    apply(pluginId)
    withPlugin(pluginId, configuration)
}
