package md.vnastasi.plugin.support

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.the

val Project.libs: VersionCatalog
    get() = the<VersionCatalogsExtension>().find("libs").get()

fun VersionCatalog.requireVersion(alias: String) = this.findVersion(alias).get().requiredVersion

fun VersionCatalog.requireLibrary(alias: String) = this.findLibrary(alias).get()

fun VersionCatalog.requirePlugin(alias: String) = this.findPlugin(alias).get()
