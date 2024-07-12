package md.vnastasi.plugin.abs

import md.vnastasi.plugin.abs.task.UpdateProjectVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.register

@Suppress("unused")
class AppBuildSupportPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.rootProject.tasks.register<UpdateProjectVersion>("updateProjectVersion") {
            versionCatalogFile.set(target.rootProject.layout.projectDirectory.dir("gradle").file("libs.versions.toml"))
        }
    }
}
