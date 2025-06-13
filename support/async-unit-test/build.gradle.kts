plugins {
    alias(libs.plugins.gradle.dependencies)
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    api(project(":support:async"))

    implementation(platform(libs.coroutines.bom))
    implementation(libs.coroutines.core)
}
