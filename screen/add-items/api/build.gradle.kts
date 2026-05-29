import md.vnastasi.plugin.support.libs

plugins {
    alias(libs.plugins.conventions.compose.library)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "md.vnastasi.shoppinglist.screen.additems.api"
}

dependencies {

    implementation(libs.navigation.runtime)
    implementation(libs.serialization.core)
}
