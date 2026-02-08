package md.vnastasi.plugin.conventions

import com.android.build.api.dsl.ApplicationExtension
import md.vnastasi.plugin.support.apply
import md.vnastasi.plugin.support.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

@Suppress("unused")
class ApplicationConventions : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        pluginManager.apply(libs.plugins.gradle.dependencies)
        pluginManager.apply(libs.plugins.android.application)
        pluginManager.apply(libs.plugins.android.cacheFix)
        pluginManager.apply(libs.plugins.kotlin.android)
        pluginManager.apply(libs.plugins.compose.compiler)

        extensions.configure<ApplicationExtension> {
            with(libs) { configureAndroid() }
            configureUnitTests()
            configureCompose()
        }

        configureKotlin()
    }
}
