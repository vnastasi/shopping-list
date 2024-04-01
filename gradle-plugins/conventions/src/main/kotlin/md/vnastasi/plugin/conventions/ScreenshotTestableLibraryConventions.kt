package md.vnastasi.plugin.conventions

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.attributes.java.TargetJvmEnvironment
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.named

@Suppress("UnstableApiUsage", "unused")
class ScreenshotTestableLibraryConventions : Plugin<Project> {

    override fun apply(target: Project) {
        target.dependencies {
            add("testRuntimeOnly", target.libs.findLibrary("junit-vintage-engine").get())
        }

        target.extensions.configure<LibraryExtension> {
            testOptions {
                unitTests {
                    all { test ->
                        test.useJUnitPlatform {
                            includeEngines = setOf("junit-jupiter", "junit-vintage")
                        }
                    }
                }
            }
        }

        val paparazziPluginId = target.libs.findPlugin("paparazzi").get().get().pluginId
        target.pluginManager.apply(paparazziPluginId)
        target.pluginManager.withPlugin(paparazziPluginId) {
            target.afterEvaluate {
                dependencies.constraints {
                    add("testImplementation", "com.google.guava:guava") {
                        attributes {
                            attribute(
                                TargetJvmEnvironment.TARGET_JVM_ENVIRONMENT_ATTRIBUTE,
                                objects.named<TargetJvmEnvironment>(TargetJvmEnvironment.STANDARD_JVM)
                            )
                        }
                        because("LayoutLib and sdk-common depend on Guava's -jre published variant. See https://github.com/cashapp/paparazzi/issues/906.")
                    }
                }
            }
        }
    }
}
