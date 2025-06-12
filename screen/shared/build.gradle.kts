import md.vnastasi.plugin.support.libs
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("compose-screen-library.conventions")
}

android {
    namespace = "md.vnastasi.shoppinglist.screen.shared"
}

dependencies {
    implementation(platform(libs.compose.bom))
    implementation(platform(libs.kotlin.bom))
    implementation(platform(libs.coroutines.bom))

    api(libs.compose.foudation.layout)
    api(libs.compose.runtime)
    api(libs.coroutines.core)

    implementation(project(":resources"))
    implementation(project(":support:annotation"))
    implementation(project(":support:theme") )

    implementation(libs.androidx.annotation)
    implementation(libs.androidx.core)
    implementation(libs.androidx.core)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.lifecycle.viewmodel.savedstate)
    implementation(libs.window.core)
    implementation(libs.compose.material)
    implementation(libs.compose.material.adaptive)
    implementation(libs.compose.preview)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.text)
    implementation(libs.collections.immutable)
    implementation(libs.lottie)
    implementation(libs.lottie.compose)

    debugImplementation(libs.compose.tooling)

    debugRuntimeOnly(libs.compose.test.manifest)

    testImplementation(platform(libs.coroutines.bom))

    testImplementation(libs.kotlin.reflect)
    testImplementation(libs.coroutines.test)

    testRuntimeOnly(libs.junit.jupiter.engine)
    testRuntimeOnly(libs.junit.platform.launcher)
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        freeCompilerArgs.addAll(
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api"
        )
    }
}
