import md.vnastasi.plugin.support.libs
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.conventions.compose.screen.library)
    alias(libs.plugins.conventions.screenshot.testing)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "md.vnastasi.shoppinglist.screen.listdetails.impl"
}

dependencies {
    compileOnly(project(":support:annotation"))

    api(project(":domain:api"))
    api(platform(libs.compose.bom))
    api(platform(libs.coroutines.bom))
    api(libs.compose.foudation.layout)
    api(libs.compose.runtime)
    api(libs.coroutines.core)
    api(libs.dagger)
    api(libs.javax.inject)

    implementation(project(":resources"))
    implementation(project(":screen:add-items:api"))
    implementation(project(":screen:list-details:api"))
    implementation(project(":screen:shared"))
    implementation(project(":support:theme"))
    implementation(platform(libs.compose.bom))
    implementation(platform(libs.coroutines.bom))
    implementation(platform(libs.kotlin.bom))
    implementation(libs.collections.immutable)
    implementation(libs.compose.animations)
    implementation(libs.compose.animations.core)
    implementation(libs.compose.foudation)
    implementation(libs.compose.graphics)
    implementation(libs.compose.material)
    implementation(libs.compose.material.icons)
    implementation(libs.compose.preview)
    implementation(libs.compose.runtime.annotation)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.text)
    implementation(libs.compose.ui.unit)
    implementation(libs.coroutines.core)
    implementation(libs.hilt.android)
    implementation(libs.hilt.core)
    implementation(libs.lifecycle.common)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.reorderable)
    implementation(libs.navigation.runtime)
    implementation(libs.hilt.viewmodel.compose)

    debugImplementation(libs.compose.tooling)

    debugRuntimeOnly(libs.compose.test.manifest)

    ksp(libs.bundles.hilt.compiler)

    testImplementation(testFixtures(project(":domain:api")))
    testImplementation(platform(libs.coroutines.bom))
    testImplementation(libs.assertk)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.mockk)
    testImplementation(libs.mockk.core)
    testImplementation(libs.mockk.dsl)
    testImplementation(libs.turbine)

    testRuntimeOnly(libs.junit.jupiter.engine)
    testRuntimeOnly(libs.junit.platform.launcher)
    testRuntimeOnly(libs.kotlin.reflect)

    screenshotTestImplementation(testFixtures(project(":domain:api")))
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
