plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.screenshot.gradlePlugin)

    implementation(project(":plugin-support"))
}

gradlePlugin {
    plugins {
        register("ScreenshotTestingConventions") {
            id = "screenshot-testing.conventions"
            implementationClass = "md.vnastasi.plugin.screenshottesting.ScreenShotTestingConvention"
        }
    }
}