import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("compose-screen-library.conventions")
    id("screenshot-testable-library.conventions")
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "md.vnastasi.shoppinglist.screen.overview"
}

dependencies {
    compileOnly(projects.support.annotation)

    api(projects.domain.api)
    api(projects.screen.shared)
    api(libs.compose.foudation.layout)
    api(libs.compose.runtime)
    api(libs.coroutines.core)
    api(libs.dagger)

    implementation(projects.resources)
    implementation(projects.support.theme)
    implementation(platform(libs.compose.bom))
    implementation(platform(libs.coroutines.bom))
    implementation(libs.collections.immutable)
    implementation(libs.compose.animations.core)
    implementation(libs.compose.foudation)
    implementation(libs.compose.graphics)
    implementation(libs.compose.material)
    implementation(libs.compose.material.icons)
    implementation(libs.compose.preview)
    implementation(libs.compose.runtime.annotation)
    implementation(libs.compose.runtime.saveable)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.text)
    implementation(libs.compose.ui.unit)
    implementation(libs.hilt.android)
    implementation(libs.hilt.core)
    implementation(libs.javax.inject)
    implementation(libs.lifecycle.common)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.lifecycle.viewmodel)

    debugImplementation(libs.compose.tooling)

    debugRuntimeOnly(libs.compose.test.manifest)

    ksp(libs.hilt.compiler)

    testImplementation(testFixtures(projects.domain.api))
    testImplementation(testFixtures(projects.screen.shared))
    testImplementation(platform(libs.coroutines.bom))
    testImplementation(libs.assertk)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.kotlin.reflect)
    testImplementation(libs.mockk)
    testImplementation(libs.mockk.dsl)
    testImplementation(libs.turbine)

    testRuntimeOnly(libs.junit.jupiter.engine)
    testRuntimeOnly(libs.junit.platform.launcher)
    testRuntimeOnly(libs.junit.vintage.engine)
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        freeCompilerArgs.addAll(
            "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-opt-in=androidx.compose.ui.ExperimentalComposeUiApi",
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api"
        )
    }
}
