package md.vnastasi.plugin.screenshottesting

import com.android.build.api.dsl.LibraryExtension
import com.android.compose.screenshot.gradle.ScreenshotTestOptions
import md.vnastasi.plugin.support.applyAndConfigure
import md.vnastasi.plugin.support.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

@Suppress("unused")
class ScreenShotTestingConvention : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        pluginManager.applyAndConfigure(libs.plugins.compose.screenshot) {
            extensions.configure<ScreenshotTestOptions> {
                imageDifferenceThreshold = 0.002f
            }

            dependencies {
                add("screenshotTestImplementation", testFixtures(project(":screen:shared")))
                add("screenshotTestImplementation", libs.compose.screenshot.validation)
                add("screenshotTestImplementation", libs.compose.tooling)
            }
        }

        extensions.configure<LibraryExtension> {
            experimentalProperties["android.experimental.enableScreenshotTest"] = true
        }
    }
}
