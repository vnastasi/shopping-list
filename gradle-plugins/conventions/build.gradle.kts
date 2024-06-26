import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + setOf(
            "-Xcontext-receivers"
        )
    }
}

dependencies {
    implementation(platform(libs.kotlin.bom))
    implementation(libs.kotlin.stdlib)

    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.android.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("SimpleLibraryConventions") {
            id = "simple-library.conventions"
            implementationClass = "md.vnastasi.plugin.conventions.SimpleLibraryConventions"
        }

        register("TestableLibraryConventions") {
            id = "testable-library.conventions"
            implementationClass = "md.vnastasi.plugin.conventions.TestableLibraryConventions"
        }

        register("ScreenshotTestableLibraryConventions") {
            id = "screenshot-testable-library.conventions"
            implementationClass = "md.vnastasi.plugin.conventions.ScreenshotTestableLibraryConventions"
        }

        register("ComposeLibraryConventions") {
            id = "compose-library.conventions"
            implementationClass = "md.vnastasi.plugin.conventions.ComposeLibraryConventions"
        }

        register("ComposeScreenLibraryConventions") {
            id = "compose-screen-library.conventions"
            implementationClass = "md.vnastasi.plugin.conventions.ComposeScreenLibraryConventions"
        }

        register("ApplicationConventions") {
            id = "application.conventions"
            implementationClass = "md.vnastasi.plugin.conventions.ApplicationConventions"
        }
    }
}

