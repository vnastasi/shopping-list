package md.vnastasi.plugin.conventions

import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.dsl.TestedExtension
import dev.detekt.gradle.Detekt
import md.vnastasi.plugin.support.apply
import md.vnastasi.plugin.support.libs
import md.vnastasi.plugin.support.requirePlugin
import md.vnastasi.plugin.support.requireVersion
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.configureKotlin() {
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(libs.requireVersion("project-jdk")))
        }
    }
}

context(libs: VersionCatalog)
internal fun CommonExtension.configureAndroid() {
    compileSdk = libs.requireVersion("project-compileSdk").toInt()

    defaultConfig.apply {
        minSdk {
            version = release(libs.requireVersion("project-minSdk").toInt())
        }
    }

    compileOptions.apply {
        sourceCompatibility = JavaVersion.toVersion(libs.requireVersion("project-jdk"))
        targetCompatibility = JavaVersion.toVersion(libs.requireVersion("project-jdk"))
    }

    buildTypes.apply {
        getByName("debug") {
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
        }
    }

    testCoverage.apply {
        jacocoVersion = libs.requireVersion("jacoco")
    }
}

internal fun CommonExtension.configureUnitTests() {
    testOptions.apply {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
            all { test ->
                test.jvmArgs("-Xmx2g", "-XX:+UseG1GC")
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
    pluginManager.apply(libs.requirePlugin("gradle-dependency-analysis"))
    pluginManager.apply(libs.requirePlugin("gradle-dependency-sort"))
    pluginManager.apply(libs.requirePlugin("android-library"))
    pluginManager.apply(libs.requirePlugin("android-cacheFix"))

    extensions.configure<LibraryExtension> {
        with(libs) { configureAndroid() }
        enableTestFixtures()
    }

    configureKotlin()
    configureDetekt()
}

internal fun Project.configureTestableLibrary() {
    extensions.configure<LibraryExtension> {
        configureUnitTests()
        enableTestFixtures()
    }
}

internal fun Project.configureComposeLibrary() {
    configureSimpleLibrary()

    pluginManager.apply(libs.requirePlugin("compose-compiler"))

    extensions.configure<LibraryExtension> {
        configureCompose()
    }
}

internal fun Project.configureDetekt() {
    pluginManager.apply(libs.requirePlugin("detekt2"))

    tasks.withType<Detekt>().configureEach {
        jvmTarget.set(libs.requireVersion("project-jdk"))

        reports {
            html.required.set(true)
        }

        exclude("**/support/icon/**")
    }
}

@Suppress("UnstableApiUsage")
internal fun TestedExtension.enableTestFixtures() {
    testFixtures {
        enable = true
    }
}
