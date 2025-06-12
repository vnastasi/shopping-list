plugins {
    id("simple-library.conventions")
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "md.vnastasi.shoppinglist.domain.api"
}

dependencies {
    implementation(platform(libs.kotlin.bom))
    implementation(platform(libs.coroutines.bom))

    implementation(libs.kotlin.parcelize)
    implementation(libs.coroutines.core)
}
