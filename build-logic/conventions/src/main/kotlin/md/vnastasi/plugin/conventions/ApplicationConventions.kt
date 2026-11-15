package md.vnastasi.plugin.conventions

import com.android.build.api.dsl.ApplicationExtension
import md.vnastasi.plugin.support.apply
import md.vnastasi.plugin.support.libs
import md.vnastasi.plugin.support.requirePlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

@Suppress("unused")
class ApplicationConventions : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        pluginManager.apply(libs.requirePlugin("gradle-dependency-analysis"))
        pluginManager.apply(libs.requirePlugin("gradle-dependency-sort"))
        pluginManager.apply(libs.requirePlugin("android-application"))
        pluginManager.apply(libs.requirePlugin("android-cacheFix"))
        pluginManager.apply(libs.requirePlugin("compose-compiler"))

        extensions.configure<ApplicationExtension> {
            with(libs) { configureAndroid() }
            configureUnitTests()
            configureCompose()
        }

        configureKotlin()
        configureDetekt()
    }
}
