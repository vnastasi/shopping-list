package md.vnastasi.plugin.detektaggregator

import dev.detekt.gradle.Detekt
import md.vnastasi.plugin.support.apply
import md.vnastasi.plugin.support.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

@Suppress("unused")
class DetektAggregatorPlugin : Plugin<Project> {

    override fun apply(target: Project): Unit = with(target) {
        val plugin = libs.plugins.detekt2

        rootProject.subprojects {
            pluginManager.apply(plugin)

            tasks.withType<Detekt>().configureEach {
                jvmTarget.set(libs.versions.project.jdk.get())

                reports {
                    html.required.set(true)
                }

                exclude("**/support/icon/**")
            }
        }
    }
}
