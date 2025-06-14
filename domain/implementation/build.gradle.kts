plugins {
    id("simple-library.conventions")
    id("testable-library.conventions")
}

android {
    namespace = "md.vnastasi.shoppinglist.domain.impl"
}

dependencies {
    implementation(projects.database)
    implementation(projects.domain.api)

    implementation(platform(libs.coroutines.bom))
    implementation(platform(libs.kotlin.bom))

    implementation(libs.coroutines.core)
    implementation(libs.koin.core)

    testImplementation(testFixtures(projects.database))
    testImplementation(testFixtures(projects.domain.api))

    testImplementation(platform(libs.coroutines.bom))

    testImplementation(libs.assertk)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.kotlin.reflect)
    testImplementation(libs.mockk)
    testImplementation(libs.mockk.dsl)
    testImplementation(libs.turbine)

    testRuntimeOnly(libs.junit.jupiter.engine)
    testRuntimeOnly(libs.junit.platform.launcher)
}
