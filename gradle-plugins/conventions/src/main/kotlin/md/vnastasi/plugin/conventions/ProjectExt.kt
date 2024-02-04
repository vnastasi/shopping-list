package md.vnastasi.plugin.conventions

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal fun Project.configureKotlin() {
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_17.toString()
            freeCompilerArgs = freeCompilerArgs + setOf(
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-opt-in=androidx.compose.ui.ExperimentalComposeUiApi",
                "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api"
            )
        }
    }
}

context(Project)
internal fun CommonExtension<*, *, *, *, *>.configureAndroid() {
    compileSdk = libs.findVersion("project-compileSdk").get().requiredVersion.toInt()

    defaultConfig {
        minSdk = libs.findVersion("project-minSdk").get().requiredVersion.toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

internal fun CommonExtension<*, *, *, *, *>.configureUnitTests() {
    @Suppress("UnstableApiUsage")
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
            all {
                it.useJUnitPlatform()
            }
        }
    }
}

context(Project)
internal fun CommonExtension<*, *, *, *, *>.configureCompose() {
    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.findVersion("compose-compiler").get().requiredVersion
    }
}

internal fun Project.addUnitTestDependencies() {
    dependencies {
        add("testImplementation", platform(libs.findLibrary("kotlinx-coroutines-bom").get()))
        add("testImplementation", libs.findLibrary("assertk").get())
        add("testImplementation", libs.findLibrary("junit-jupiter").get())
        add("testImplementation", libs.findLibrary("kotlin-reflect").get())
        add("testImplementation", libs.findLibrary("kotlinx-coroutines-test").get())
        add("testImplementation", libs.findLibrary("mockito-kotlin").get())
        add("testImplementation", libs.findLibrary("turbine").get())
        add("testRuntimeOnly", libs.findLibrary("junit-jupiter-engine").get())
    }
}

internal fun Project.configureSimpleLibrary() {
    pluginManager.apply(libs.findPlugin("android-library").get().get().pluginId)
    pluginManager.apply(libs.findPlugin("android-cacheFix").get().get().pluginId)
    pluginManager.apply(libs.findPlugin("kotlin-android").get().get().pluginId)

    extensions.configure<LibraryExtension> {
        configureAndroid()
    }

    configureKotlin()
}

internal fun Project.configureTestableLibrary() {
    extensions.configure<LibraryExtension> {
        configureUnitTests()
    }
    addUnitTestDependencies()
}

internal fun Project.configureComposeLibrary() {
    configureSimpleLibrary()
    extensions.configure<LibraryExtension> {
        configureCompose()
    }
}

internal fun Project.configureComposeScreenLibrary() {
    configureComposeLibrary()
    configureTestableLibrary()
    dependencies {
        add("implementation", project(":domain:api"))
        add("implementation", project(":resources"))
        add("implementation", project(":support:async"))
        add("implementation", project(":support:theme"))
        add("implementation", project(":support:ui"))
        add("implementation", platform(libs.findLibrary("compose-bom").get()))
        add("implementation", platform(libs.findLibrary("kotlin-bom").get()))
        add("implementation", platform(libs.findLibrary("kotlinx-coroutines-bom").get()))
        add("implementation", libs.findLibrary("androidx-core").get())
        add("implementation", libs.findLibrary("koin-android").get())
        add("implementation", libs.findLibrary("kotlinx-collections").get())
        add("implementation", libs.findBundle("compose-ui").get())
        add("debugImplementation", libs.findBundle("compose-debug").get())
        add("testImplementation", project(":domain:test-data"))
        add("testImplementation", project(":support:async-unit-test"))
    }
}

internal fun Project.configureApplication() {
    pluginManager.apply(libs.findPlugin("android-application").get().get().pluginId)
    pluginManager.apply(libs.findPlugin("android-cacheFix").get().get().pluginId)
    pluginManager.apply(libs.findPlugin("kotlin-android").get().get().pluginId)

    extensions.configure<ApplicationExtension> {
        configureAndroid()
        configureUnitTests()
        configureCompose()
    }

    addUnitTestDependencies()
    configureKotlin()
}
