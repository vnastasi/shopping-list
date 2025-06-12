import md.vnastasi.plugin.support.libs

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
    implementation(platform(libs.coroutines.bom))

    implementation(libs.koin.core)
    implementation(libs.coroutines.core)

    testImplementation(testFixtures(project(":database")))
    testImplementation(testFixtures(project(":domain:api")))

    testImplementation(platform(libs.coroutines.bom))

    testImplementation(libs.assertk)
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.kotlin.reflect)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(libs.mockk.dsl)
    testImplementation(libs.turbine)

    testRuntimeOnly(libs.junit.jupiter.engine)
    testRuntimeOnly(libs.junit.platform.launcher)
}
