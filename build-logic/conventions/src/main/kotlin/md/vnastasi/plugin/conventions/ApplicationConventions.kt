package md.vnastasi.plugin.conventions

import org.gradle.api.Plugin
import org.gradle.api.Project

@Suppress("unused")
class ApplicationConventions : Plugin<Project> {

    override fun apply(target: Project) {
        target.configureApplication()
    }
}
