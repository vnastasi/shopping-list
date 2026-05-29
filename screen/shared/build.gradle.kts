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
    compileOnly(project(":support:annotation"))

    api(platform(libs.compose.bom))
    api(platform(libs.coroutines.bom))
    api(libs.compose.foudation.layout)
    api(libs.compose.runtime)
    api(libs.coroutines.core)
    api(libs.dagger)
    api(libs.lifecycle.viewmodel)
    api(libs.compose.animations)

    implementation(project(":resources"))
    implementation(project(":support:theme"))
    implementation(platform(libs.compose.bom))
    implementation(platform(libs.coroutines.bom))
    implementation(platform(libs.kotlin.bom))
    implementation(libs.androidx.annotation)
    api(libs.collections.immutable)
    implementation(libs.compose.foudation)
    api(libs.compose.material)
    implementation(libs.compose.material.adaptive)
    implementation(libs.compose.preview)
    implementation(libs.compose.runtime.annotation)
    api(libs.compose.ui)
    implementation(libs.compose.ui.text)
    implementation(libs.hilt.core)
    implementation(libs.hilt.android)
    implementation(libs.lottie)
    implementation(libs.lottie.compose)
    api(libs.reorderable)
    api(libs.window.core)
    implementation(libs.navigation.runtime)
    api(libs.navigation.ui)
    implementation(libs.compose.animations.core)
    implementation(libs.compose.graphics)
    implementation(libs.compose.ui.unit)

    debugApi(libs.reorderable.debug)

    ksp(libs.bundles.hilt.compiler)

    debugImplementation(libs.compose.tooling)

    debugRuntimeOnly(libs.compose.test.manifest)

    testFixturesCompileOnly(libs.compose.tooling)
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        freeCompilerArgs.addAll(
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
            "-Xcontext-parameters"
        )
    }
}
