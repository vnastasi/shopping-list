import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("compose-screen-library.conventions")
    id("screenshot-testable-library.conventions")
}

android {
    namespace = "md.vnastasi.shoppinglist.screen.listdetails"
}

dependencies {
    implementation(platform(libs.compose.bom))
    implementation(platform(libs.coroutines.bom))
    implementation(platform(libs.kotlin.bom))

    api(project(":domain:api"))
    api(project(":screen:shared"))

    api(libs.compose.foudation.layout)
    api(libs.compose.runtime)
    api(libs.coroutines.core)

    implementation(project(":resources"))
    implementation(project(":support:annotation"))
    implementation(project(":support:async"))
    implementation(project(":support:theme"))

    implementation(libs.collections.immutable)
    implementation(libs.compose.animations)
    implementation(libs.compose.foudation)
    implementation(libs.compose.graphics)
    implementation(libs.compose.material)
    implementation(libs.compose.material.icons)
    implementation(libs.compose.preview)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.text)
    implementation(libs.compose.ui.unit)
    implementation(libs.coroutines.core)
    implementation(libs.koin.core)
    implementation(libs.lifecycle.common)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.viewmodel.savedstate)

    debugImplementation(libs.compose.tooling)

    debugRuntimeOnly(libs.compose.test.manifest)

    testImplementation(platform(libs.coroutines.bom))

    testImplementation(project(":support:async-unit-test"))

    testImplementation(testFixtures(project(":domain:api")))

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
