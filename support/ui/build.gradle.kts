import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("compose-library.conventions")
}

android {
    namespace = "md.vnastasi.shoppinglist.support.ui"
}

dependencies {

    implementation(platform(libs.kotlin.bom))
    implementation(platform(libs.compose.bom))
    implementation(platform(libs.kotlinx.coroutines.bom))
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.collections)
    implementation(libs.androidx.core)
    implementation(libs.androidx.lificycle.runtime)
    implementation(libs.androidx.lificycle.runtime.compose)
    implementation(libs.androidx.lificycle.viewmodel.compose)
    implementation(libs.compose.material)
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + setOf(
            "-opt-in=androidx.compose.ui.ExperimentalComposeUiApi",
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api"
        )
    }
}
