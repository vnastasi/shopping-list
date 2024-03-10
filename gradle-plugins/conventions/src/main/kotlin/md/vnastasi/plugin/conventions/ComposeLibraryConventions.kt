package md.vnastasi.plugin.conventions

import org.gradle.api.Plugin
import org.gradle.api.Project

class ComposeLibraryConventions : Plugin<Project> {

    override fun apply(target: Project) {
        target.configureComposeLibrary()
        target.includeOptIns()
    }
}