plugins {
    id("simple-library.conventions")
    id("testable-library.conventions")
}

android {
    namespace = "md.vnastasi.shoppinglist.domain"
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

    testImplementation(platform(libs.kotlinx.coroutines.bom))
    testImplementation(libs.assertk)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.kotlin.reflect)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.turbine)

    testRuntimeOnly(libs.junit.jupiter.engine)
}
