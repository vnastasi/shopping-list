plugins {
    id("simple-library.conventions")
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "md.vnastasi.shoppinglist.domain.api"
}

dependencies {
    implementation(platform(libs.coroutines.bom))
    implementation(platform(libs.kotlin.bom))

    implementation(libs.coroutines.core)
    implementation(libs.kotlin.parcelize)
}
