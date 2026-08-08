package md.vnastasi.plugin.detektaggregator

import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings

@Suppress("unused")
class ApplyDetektAggregatorPlugin : Plugin<Settings> {

    override fun apply(target: Settings) {
        target.gradle.beforeProject {
            this.plugins.apply("detekt-aggregator")
        }
    }
}
