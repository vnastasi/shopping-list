package md.vnastasi.plugin.screenshottesting

import com.android.build.api.dsl.LibraryExtension
import com.android.compose.screenshot.tasks.PreviewScreenshotUpdateTask
import com.android.compose.screenshot.tasks.PreviewScreenshotValidationTask
import md.vnastasi.plugin.support.applyAndConfigure
import md.vnastasi.plugin.support.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.ProviderFactory
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import javax.inject.Inject

private const val PROPERTY_SCREENSHOT_DIFF_THRESHOLD = "SCREENSHOT_DIFF_THRESHOLD"
private const val DEFAULT_SCREENSHOT_DIFF_THRESHOLD = 0.001f

@Suppress("unused", "UnstableApiUsage")
class ScreenShotTestingConvention @Inject constructor(
    private val providers: ProviderFactory
) : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        extensions.configure<LibraryExtension> {
            experimentalProperties["android.experimental.enableScreenshotTest"] = true
        }

        pluginManager.applyAndConfigure(libs.plugins.compose.screenshot) {
            dependencies {
                add("screenshotTestImplementation", testFixtures(project(":screen:shared")))
                add("screenshotTestImplementation", libs.compose.screenshot.validation)
                add("screenshotTestImplementation", libs.compose.tooling)
            }

            val threshold = providers.environmentVariable(PROPERTY_SCREENSHOT_DIFF_THRESHOLD).map { it.toFloat() }.orElse(provider { DEFAULT_SCREENSHOT_DIFF_THRESHOLD })

            tasks.withType<PreviewScreenshotUpdateTask>().configureEach {
                testEngineInput.threshold.set(threshold)
            }

            tasks.withType<PreviewScreenshotValidationTask>().configureEach {
                testEngineInput.threshold.set(threshold)
            }
        }
    }
}
