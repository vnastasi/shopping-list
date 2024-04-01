import app.cash.paparazzi.gradle.PaparazziPlugin
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("compose-screen-library.conventions")
    alias(libs.plugins.paparazzi)
}

android {
    namespace = "md.vnastasi.shoppinglist.screen.overview"
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + setOf(
            "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-opt-in=androidx.compose.ui.ExperimentalComposeUiApi",
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api"
        )
    }
}

plugins.withId(libs.plugins.paparazzi.get().pluginId) {
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