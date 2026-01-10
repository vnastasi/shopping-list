package md.vnastasi.plugin.codecoverage

import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.listProperty
import org.gradle.kotlin.dsl.property
import javax.inject.Inject

abstract class CodeCoverageExtension @Inject constructor(objectFactory: ObjectFactory) {

    val targetBuildType: Property<String> = objectFactory.property()

    val reportDirectory: DirectoryProperty = objectFactory.directoryProperty()

    val coverageThreshold: Property<Int> = objectFactory.property()

    val excludedModules: ListProperty<Project> = objectFactory.listProperty()

    val excludedClasses: ListProperty<String> = objectFactory.listProperty()
}
