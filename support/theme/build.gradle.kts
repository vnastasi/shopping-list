import md.vnastasi.plugin.support.libs

plugins {
    id("compose-library.conventions")
}

android {
    namespace = "md.vnastasi.shoppinglist.support.theme"
}

dependencies {
    api(platform(libs.compose.bom))
    api(libs.compose.runtime)

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.foudation)
    implementation(libs.compose.graphics)
    implementation(libs.compose.material)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.text)
    implementation(libs.compose.ui.unit)
}
