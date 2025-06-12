plugins {
    alias(libs.plugins.gradle.dependencies)
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(project(":support:annotation"))

    implementation(platform(libs.kotlin.bom))
    implementation(platform(libs.kotlinx.coroutines.bom))
    implementation(libs.koin.core)
    implementation(libs.kotlinx.coroutines.core)
}
