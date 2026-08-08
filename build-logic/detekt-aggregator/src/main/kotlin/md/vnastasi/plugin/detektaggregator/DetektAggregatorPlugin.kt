package md.vnastasi.plugin.detektaggregator

import io.gitlab.arturbosch.detekt.Detekt
import md.vnastasi.plugin.support.libs
import md.vnastasi.plugin.support.requirePlugin
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

@Suppress("unused")
class DetektAggregatorPlugin : Plugin<Project> {

    override fun apply(target: Project): Unit = with(target) {
        plugins.apply(libs.requirePlugin("detekt").get().pluginId)

        tasks.withType<Detekt>().configureEach {
            jvmTarget = JavaVersion.VERSION_17.toString()

            reports {
                html.required.set(true)
                xml.required.set(false)
                sarif.required.set(false)
                md.required.set(false)
                txt.required.set(false)
            }

            exclude("**/support/icon/**")
        }
    }
}
