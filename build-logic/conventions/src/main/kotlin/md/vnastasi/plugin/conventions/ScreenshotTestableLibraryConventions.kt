package md.vnastasi.plugin.conventions

import com.android.build.gradle.LibraryExtension
import md.vnastasi.plugin.support.applyAndConfigure
import md.vnastasi.plugin.support.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.attributes.java.TargetJvmEnvironment
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.named

@Suppress("UnstableApiUsage", "unused")
class ScreenshotTestableLibraryConventions : Plugin<Project> {

    override fun apply(target: Project): Unit = with(target) {
//        dependencies {
//            addProvider("testRuntimeOnly", libs.junit.vintage.engine)
//        }

        extensions.configure<LibraryExtension> {
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

        pluginManager.applyAndConfigure(libs.plugins.paparazzi) {
            afterEvaluate {
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
