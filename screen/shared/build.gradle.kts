import md.vnastasi.plugin.support.libs
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.conventions.compose.library)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "md.vnastasi.shoppinglist.screen.shared"
}

dependencies {
    api(platform(libs.compose.bom))
    api(platform(libs.coroutines.bom))
    api(libs.collections.immutable)
    api(libs.compose.animations)
    api(libs.compose.foudation.layout)
    api(libs.compose.material)
    api(libs.compose.runtime)
    api(libs.compose.ui)
    api(libs.coroutines.core)
    api(libs.dagger)
    api(libs.lifecycle.viewmodel)
    api(libs.navigation.ui)
    api(libs.reorderable)
    api(libs.window.core)

    debugApi(libs.reorderable.debug)

    implementation(platform(libs.compose.bom))
    implementation(platform(libs.coroutines.bom))
    implementation(platform(libs.kotlin.bom))
    implementation(project(":resources"))
    implementation(project(":support:theme"))
    implementation(libs.androidx.annotation)
    implementation(libs.compose.animations.core)
    implementation(libs.compose.foudation)
    implementation(libs.compose.graphics)
    implementation(libs.compose.material.adaptive)
    implementation(libs.compose.preview)
    implementation(libs.compose.runtime.annotation)
    implementation(libs.compose.ui.text)
    implementation(libs.compose.ui.unit)
    implementation(libs.hilt.android)
    implementation(libs.hilt.core)
    implementation(libs.lottie)
    implementation(libs.lottie.compose)
    implementation(libs.navigation.runtime)

    debugImplementation(libs.compose.tooling)

    compileOnly(project(":support:annotation"))

    testFixturesCompileOnly(libs.compose.tooling)

    debugRuntimeOnly(libs.compose.test.manifest)

    ksp(libs.bundles.hilt.compiler)
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        freeCompilerArgs.addAll(
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api"
        )
    }
}
