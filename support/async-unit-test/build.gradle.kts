plugins {
    alias(libs.plugins.gradle.dependencies)
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    api(project(":support:async"))

    implementation(platform(libs.kotlinx.coroutines.bom))
    implementation(libs.kotlinx.coroutines.core)
}
