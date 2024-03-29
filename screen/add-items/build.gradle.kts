import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("compose-screen-library.conventions")
}

android {
    namespace = "md.vnastasi.shoppinglist.screen.additems"
}

dependencies {

    testImplementation(libs.androidx.lificycle.test)
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
