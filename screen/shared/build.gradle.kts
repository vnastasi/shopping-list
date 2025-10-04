import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("compose-library.conventions")
//    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "md.vnastasi.shoppinglist.screen.shared"
}

dependencies {
    compileOnly(projects.support.annotation)

    api(libs.compose.foudation.layout)
    api(libs.compose.runtime)
    api(libs.coroutines.core)
    api(libs.dagger)

    implementation(projects.resources)
    implementation(projects.support.theme)
    implementation(platform(libs.compose.bom))
    implementation(platform(libs.coroutines.bom))
    implementation(platform(libs.kotlin.bom))
    implementation(libs.androidx.annotation)
    implementation(libs.collections.immutable)
    implementation(libs.compose.foudation)
    implementation(libs.compose.material)
    implementation(libs.compose.material.adaptive)
    implementation(libs.compose.preview)
    implementation(libs.compose.runtime.annotation)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.text)
    implementation(libs.hilt.core)
    implementation(libs.lottie)
    implementation(libs.lottie.compose)
    implementation(libs.window.core)

    ksp(libs.hilt.compiler)

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
