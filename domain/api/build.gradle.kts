plugins {
    alias(libs.plugins.conventions.simple.library)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "md.vnastasi.shoppinglist.domain.api"
}

dependencies {
    api(platform(libs.coroutines.bom))
    api(libs.coroutines.core)

    implementation(platform(libs.kotlin.bom))
    implementation(libs.kotlin.parcelize)
}
