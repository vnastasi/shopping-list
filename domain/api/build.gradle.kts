plugins {
    id("simple-library.conventions")
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "md.vnastasi.shoppinglist.domain.api"
}

dependencies {

    implementation(platform(libs.kotlin.bom))
    implementation(platform(libs.compose.bom))
    implementation(platform(libs.kotlinx.coroutines.bom))
    implementation(libs.compose.runtime)
    implementation(libs.kotlinx.coroutines.core)
}
