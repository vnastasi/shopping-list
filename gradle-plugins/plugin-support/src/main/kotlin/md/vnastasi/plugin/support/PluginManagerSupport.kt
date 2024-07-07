package md.vnastasi.plugin.support

import org.gradle.api.plugins.AppliedPlugin
import org.gradle.api.plugins.PluginManager
import org.gradle.api.provider.Provider
import org.gradle.plugin.use.PluginDependency

fun PluginManager.apply(plugin: Provider<PluginDependency>) {
    apply(plugin.get().pluginId)
}

fun PluginManager.withPlugin(plugin: Provider<PluginDependency>, configuration: AppliedPlugin.() -> Unit) {
    withPlugin(plugin.get().pluginId, configuration)
}

fun PluginManager.applyAndConfigure(plugin: Provider<PluginDependency>, configuration: AppliedPlugin.() -> Unit) {
    val pluginId = plugin.get().pluginId
    apply(pluginId)
    withPlugin(pluginId, configuration)
}
