plugins {
    id("compose-library.conventions")
}

android {
    namespace = "md.vnastasi.shoppinglist.support.theme"
}

dependencies {
    implementation(platform(libs.compose.bom))

    api(libs.compose.runtime)

    implementation(libs.compose.foudation)
    implementation(libs.compose.graphics)
    implementation(libs.compose.material)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.text)
    implementation(libs.compose.ui.unit)
}
