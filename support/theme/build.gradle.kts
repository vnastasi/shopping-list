plugins {
    id("compose-library.conventions")
}

android {
    namespace = "md.vnastasi.shoppinglist.support.theme"
}

dependencies {

    implementation(platform(libs.compose.bom))
    implementation(libs.androidx.core)
    implementation(libs.compose.material)
}
