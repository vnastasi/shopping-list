plugins {
    id("compose-library.conventions")
    id("testable-library.conventions")
}

android {
    namespace = "md.vnastasi.shoppinglist.screen.listdetails"
}

dependencies {

    implementation(project(":domain:api"))
    implementation(project(":resources"))
    implementation(project(":support:async"))
    implementation(project(":support:theme"))
    implementation(project(":support:ui"))

    implementation(platform(libs.compose.bom))
    implementation(platform(libs.kotlin.bom))
    implementation(platform(libs.kotlinx.coroutines.bom))
    implementation(libs.androidx.core)
    implementation(libs.androidx.lificycle.runtime)
    implementation(libs.androidx.lificycle.runtime.compose)
    implementation(libs.compose.graphics)
    implementation(libs.compose.material)
    implementation(libs.compose.preview)
    implementation(libs.compose.ui)
    implementation(libs.koin.android)
    implementation(libs.kotlinx.collections)

    debugImplementation(libs.compose.test.manifest)
    debugImplementation(libs.compose.tooling)

    testImplementation(project(":domain:test-data"))
    testImplementation(project(":support:async-unit-test"))

    testImplementation(platform(libs.kotlinx.coroutines.bom))
    testImplementation(libs.androidx.lificycle.test)
    testImplementation(libs.assertk)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.kotlin.reflect)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.turbine)

    testRuntimeOnly(libs.junit.jupiter.engine)
}
