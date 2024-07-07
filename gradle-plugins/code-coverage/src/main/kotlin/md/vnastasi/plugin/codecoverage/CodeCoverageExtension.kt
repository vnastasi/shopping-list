package md.vnastasi.plugin.codecoverage

import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property

interface CodeCoverageExtension {

    val targetBuildType: Property<String>

    val reportDirectory: DirectoryProperty

    val coverageThreshold: Property<Double>
}
