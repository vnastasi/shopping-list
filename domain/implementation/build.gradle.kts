plugins {
    id("simple-library.conventions")
    id("testable-library.conventions")
}

android {
    namespace = "md.vnastasi.shoppinglist.domain.impl"
}

dependencies {

    implementation(project(":database:implementation"))
    implementation(project(":domain:api"))

    implementation(platform(libs.kotlin.bom))
    implementation(platform(libs.kotlinx.coroutines.bom))
    implementation(libs.koin.android)
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(project(":database:test-data"))
    testImplementation(project(":domain:test-data"))
}
