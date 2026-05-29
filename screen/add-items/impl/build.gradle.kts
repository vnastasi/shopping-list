import md.vnastasi.plugin.support.libs
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.conventions.compose.screen.library)
    alias(libs.plugins.conventions.screenshot.testing)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "md.vnastasi.shoppinglist.screen.additems.impl"
}

dependencies {
    api(platform(libs.compose.bom))
    api(platform(libs.coroutines.bom))
    api(project(":domain:api"))
    api(project(":screen:shared"))
    api(libs.collections.immutable)
    api(libs.compose.foudation)
    api(libs.compose.foudation.layout)
    api(libs.compose.runtime)
    api(libs.coroutines.core)
    api(libs.dagger)
    api(libs.javax.inject)
    api(libs.lifecycle.viewmodel)
    api(libs.navigation.runtime)

    implementation(platform(libs.compose.bom))
    implementation(platform(libs.coroutines.bom))
    implementation(platform(libs.kotlin.bom))
    implementation(project(":resources"))
    implementation(project(":screen:add-items:api"))
    implementation(project(":support:theme"))
    implementation(libs.compose.animations)
    implementation(libs.compose.animations.core)
    implementation(libs.compose.graphics)
    implementation(libs.compose.material)
    implementation(libs.compose.material.icons)
    implementation(libs.compose.preview)
    implementation(libs.compose.runtime.annotation)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.text)
    implementation(libs.compose.ui.unit)
    implementation(libs.hilt.android)
    implementation(libs.hilt.core)
    implementation(libs.hilt.viewmodel.compose)
    implementation(libs.lifecycle.common)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.navigation.ui)

    debugImplementation(libs.compose.tooling)

    compileOnly(project(":support:annotation"))

    debugRuntimeOnly(libs.compose.test.manifest)

    testImplementation(platform(libs.coroutines.bom))
    testImplementation(testFixtures(project(":domain:api")))
    testImplementation(libs.assertk)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.kotlin.reflect)
    testImplementation(libs.mockk)
    testImplementation(libs.mockk.core)
    testImplementation(libs.mockk.dsl)
    testImplementation(libs.turbine)

    testRuntimeOnly(libs.junit.jupiter.engine)
    testRuntimeOnly(libs.junit.platform.launcher)

    ksp(libs.bundles.hilt.compiler)
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        freeCompilerArgs.addAll(
            "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-opt-in=androidx.compose.ui.ExperimentalComposeUiApi",
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
            "-Xcontext-parameters"
        )
    }
}
