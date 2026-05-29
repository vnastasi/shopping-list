import md.vnastasi.plugin.support.libs

plugins {
    alias(libs.plugins.conventions.compose.library)
}

android {
    namespace = "md.vnastasi.shoppinglist.support.theme"
}

dependencies {
    api(platform(libs.compose.bom))
    api(libs.compose.material)
    api(libs.compose.runtime)
    api(libs.compose.ui)

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.foudation)
    implementation(libs.compose.graphics)
    implementation(libs.compose.ui.text)
    implementation(libs.compose.ui.unit)
}
