plugins {
    id("simple-library.conventions")
}

android {
    namespace = "md.vnastasi.shoppinglist.support.async"
}

dependencies {

    implementation(project(":support:annotation"))

    implementation(platform(libs.kotlin.bom))
    implementation(platform(libs.kotlinx.coroutines.bom))
    implementation(libs.koin.core)
    implementation(libs.kotlinx.coroutines.core)
}
