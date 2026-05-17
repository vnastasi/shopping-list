package md.vnastasi.plugin.screenshottesting

import com.android.build.api.dsl.LibraryExtension
import com.android.compose.screenshot.gradle.ScreenshotTestOptions
import md.vnastasi.plugin.support.applyAndConfigure
import md.vnastasi.plugin.support.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.ProviderFactory
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import javax.inject.Inject

private const val PROPERTY_SCREENSHOT_DIFF_THRESHOLD = "SCREENSHOT_DIFF_THRESHOLD"
private const val DEFAULT_SCREENSHOT_DIFF_THRESHOLD = 0.001f

@Suppress("unused")
class ScreenShotTestingConvention @Inject constructor(
    private val providers: ProviderFactory
) : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        pluginManager.applyAndConfigure(libs.plugins.compose.screenshot) {
            extensions.configure<ScreenshotTestOptions> {
                imageDifferenceThreshold = providers.environmentVariable(PROPERTY_SCREENSHOT_DIFF_THRESHOLD).map { it.toFloat() }.getOrElse(DEFAULT_SCREENSHOT_DIFF_THRESHOLD)
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
