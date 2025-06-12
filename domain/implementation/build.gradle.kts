plugins {
    id("simple-library.conventions")
    id("testable-library.conventions")
}

android {
    namespace = "md.vnastasi.shoppinglist.domain.impl"
}

dependencies {

    implementation(project(":database"))
    implementation(project(":domain:api"))

    implementation(platform(libs.kotlin.bom))
    implementation(platform(libs.kotlinx.coroutines.bom))
    implementation(libs.koin.core)
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(testFixtures(project(":database")))
    testImplementation(testFixtures(project(":domain:api")))
}
