package md.vnastasi.plugin.conventions

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.dsl.TestedExtension
import md.vnastasi.plugin.support.apply
import md.vnastasi.plugin.support.libs
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.configureKotlin() {
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(libs.versions.project.jdk.get()))
        }
    }
}

internal fun CommonExtension.configureAndroid(libs: LibrariesForLibs) {
    compileSdk = libs.versions.project.compileSdk.get().toInt()

    defaultConfig.apply {
        minSdk {
            version = release(libs.versions.project.minSdk.get().toInt())
        }
    }

    compileOptions.apply {
        sourceCompatibility = JavaVersion.toVersion(libs.versions.project.jdk.get())
        targetCompatibility = JavaVersion.toVersion(libs.versions.project.jdk.get())
    }
}

internal fun CommonExtension.configureUnitTests() {
    testOptions.apply {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
            all { test ->
                test.useJUnitPlatform()
                test.testLogging {
                    exceptionFormat = TestExceptionFormat.FULL
                }
            }
        }
    }
}

internal fun CommonExtension.configureCompose() {
    buildFeatures.apply {
        compose = true
    }
}

internal fun Project.configureSimpleLibrary() {
    pluginManager.apply(libs.plugins.gradle.dependencies)
    pluginManager.apply(libs.plugins.android.library)
    pluginManager.apply(libs.plugins.android.cacheFix)

    extensions.configure<LibraryExtension> {
        configureAndroid(libs)
        enableTestFixtures()
    }

    configureKotlin()
}

internal fun Project.configureTestableLibrary() {
    extensions.configure<LibraryExtension> {
        configureUnitTests()
        enableTestFixtures()
    }
}

internal fun Project.configureComposeLibrary() {
    configureSimpleLibrary()

    pluginManager.apply(libs.plugins.compose.compiler)

    extensions.configure<LibraryExtension> {
        configureCompose()
    }
}

internal fun Project.configureComposeScreenLibrary() {
    configureComposeLibrary()
    configureTestableLibrary()
}

internal fun Project.configureApplication() {
    pluginManager.apply(libs.plugins.gradle.dependencies)
    pluginManager.apply(libs.plugins.android.application)
    pluginManager.apply(libs.plugins.android.cacheFix)
    pluginManager.apply(libs.plugins.compose.compiler)

    extensions.configure<ApplicationExtension> {
        configureAndroid(libs)
        configureUnitTests()
        configureCompose()
    }

    configureKotlin()
}

@Suppress("UnstableApiUsage")
internal fun TestedExtension.enableTestFixtures() {
    testFixtures {
        enable = true
    }
}
