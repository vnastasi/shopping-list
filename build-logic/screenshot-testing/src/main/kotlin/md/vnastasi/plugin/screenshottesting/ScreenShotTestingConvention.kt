package md.vnastasi.plugin.screenshottesting

import com.android.build.api.dsl.LibraryExtension
import com.android.compose.screenshot.tasks.PreviewScreenshotUpdateTask
import com.android.compose.screenshot.tasks.PreviewScreenshotValidationTask
import md.vnastasi.plugin.support.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.ProviderFactory
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke
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
            testOptions {
                screenshotTests.create("screenshotTest") {
                    engineVersion = libs.versions.compose.screenshot.get()
                    targetVariants.add("debug")
                    dependencies {
                        implementation(libs.compose.tooling)
                        implementation(libs.compose.screenshot.validation)
                    }
                }
            }
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
