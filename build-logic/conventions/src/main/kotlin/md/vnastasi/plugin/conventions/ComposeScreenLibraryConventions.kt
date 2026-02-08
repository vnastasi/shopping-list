package md.vnastasi.plugin.conventions

import org.gradle.api.Plugin
import org.gradle.api.Project

@Suppress("unused")
class ComposeScreenLibraryConventions : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        configureComposeLibrary()
        configureTestableLibrary()
    }
}
