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
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.configureKotlin() {
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
}

internal fun CommonExtension<*, *, *, *, *, *>.configureAndroid(libs: LibrariesForLibs) {
    compileSdk = libs.versions.project.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.project.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

internal fun CommonExtension<*, *, *, *, *, *>.configureUnitTests() {
    @Suppress("UnstableApiUsage")
    testOptions {
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

internal fun CommonExtension<*, *, *, *, *, *>.configureCompose() {
    buildFeatures {
        compose = true
    }
}

internal fun Project.addUnitTestDependencies() {
    dependencies {
        addProvider("testImplementation", platform(libs.kotlinx.coroutines.bom))
        addProvider("testImplementation", libs.assertk)
        addProvider("testImplementation", libs.junit.jupiter.asProvider())
        addProvider("testImplementation", libs.kotlin.reflect)
        addProvider("testImplementation", libs.kotlinx.coroutines.test)
        addProvider("testImplementation", libs.mockk.agent)
        addProvider("testImplementation", libs.mockk.android)
        addProvider("testImplementation", libs.turbine)
        addProvider("testRuntimeOnly", libs.junit.jupiter.engine)
        add("testRuntimeOnly", "org.junit.platform:junit-platform-launcher")
    }
}

internal fun Project.configureSimpleLibrary() {
    pluginManager.apply(libs.plugins.gradle.dependencies)
    pluginManager.apply(libs.plugins.android.library)
    pluginManager.apply(libs.plugins.android.cacheFix)
    pluginManager.apply(libs.plugins.kotlin.android)

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
    addUnitTestDependencies()
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

    dependencies {
        addProvider("implementation", provider { project(":domain:api") })
        addProvider("implementation", provider { project(":resources") })
        addProvider("implementation", provider { project(":support:annotation") })
        addProvider("implementation", provider { project(":support:async") })
        addProvider("implementation", provider { project(":support:theme") })
        addProvider("implementation", platform(libs.compose.bom))
        addProvider("implementation", platform(libs.kotlin.bom))
        addProvider("implementation", platform(libs.kotlinx.coroutines.bom))
        addProvider("implementation", libs.androidx.core)
        addProvider("implementation", libs.koin.android)
        addProvider("implementation", libs.kotlinx.collections)
        addProvider("implementation", libs.lottie.compose)
        addProvider("implementation", libs.bundles.compose.ui)
        addProvider("debugImplementation", libs.bundles.compose.debug)
        addProvider("testImplementation", provider { project(":support:async-unit-test") })
        addProvider("testImplementation", provider { testFixtures(project(":domain:api")) })
    }
}

internal fun Project.configureApplication() {
    pluginManager.apply(libs.plugins.gradle.dependencies)
    pluginManager.apply(libs.plugins.android.application)
    pluginManager.apply(libs.plugins.android.cacheFix)
    pluginManager.apply(libs.plugins.kotlin.android)
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
