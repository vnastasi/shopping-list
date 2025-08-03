import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("compose-library.conventions")
}

android {
    namespace = "md.vnastasi.shoppinglist.screen.shared"
}

dependencies {
    implementation(platform(libs.compose.bom))
    implementation(platform(libs.coroutines.bom))
    implementation(platform(libs.kotlin.bom))

    api(libs.compose.foudation.layout)
    api(libs.compose.runtime)
    api(libs.coroutines.core)

    implementation(projects.resources)
    implementation(projects.support.annotation)
    implementation(projects.support.theme)

    implementation(libs.androidx.annotation)
    implementation(libs.androidx.core)
    implementation(libs.androidx.core)
    implementation(libs.collections.immutable)
    implementation(libs.compose.material)
    implementation(libs.compose.material.adaptive)
    implementation(libs.compose.preview)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.text)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.lifecycle.viewmodel.savedstate)
    implementation(libs.lottie)
    implementation(libs.lottie.compose)
    implementation(libs.window.core)

    debugImplementation(libs.compose.tooling)

    debugRuntimeOnly(libs.compose.test.manifest)
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        freeCompilerArgs.addAll(
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api"
        )
    }
}
