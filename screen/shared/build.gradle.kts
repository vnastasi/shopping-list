import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("compose-screen-library.conventions")
}

android {
    namespace = "md.vnastasi.shoppinglist.screen.shared"
}

dependencies {
    implementation(libs.androidx.core)
    implementation(libs.androidx.lificycle.viewmodel.compose)
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        freeCompilerArgs.addAll(
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api"
        )
    }
}
