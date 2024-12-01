import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)

    implementation(project(":plugin-support"))
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

