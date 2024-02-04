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

    testImplementation(libs.androidx.lificycle.test)
}
