package md.vnastasi.plugin.detektaggregator

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withConvention
import org.gradle.kotlin.dsl.withType

@Suppress("unused")
class DetektAggregatorPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        val libs = target.extensions.getByType<VersionCatalogsExtension>().named("libs")
        val pluginId =  libs.findPlugin("detekt").get().get().pluginId

        target.rootProject.subprojects {
            pluginManager.apply(pluginId)

            tasks.withType<Detekt>().configureEach {
                jvmTarget = JavaVersion.VERSION_17.toString()

                reports {
                    html.required.set(true)
                    xml.required.set(false)
                    sarif.required.set(false)
                    md.required.set(false)
                    txt.required.set(false)
                }
            }
        }
    }
}
