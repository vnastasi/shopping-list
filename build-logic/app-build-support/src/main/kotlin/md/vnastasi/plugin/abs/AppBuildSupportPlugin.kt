package md.vnastasi.plugin.abs

import md.vnastasi.plugin.abs.task.UpdateProjectVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.register

@Suppress("unused")
class AppBuildSupportPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        if (target.isolated != target.isolated.rootProject) {
            return
        }

        val versionCatalogFileProvider = target.isolated.rootProject.projectDirectory.dir("gradle").file("libs.versions.toml")

        target.tasks.register<UpdateProjectVersion>("updateProjectVersion") {
            versionCatalogFile.set(versionCatalogFileProvider)
        }
    }
}
