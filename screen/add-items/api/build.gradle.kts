import md.vnastasi.plugin.support.libs

plugins {
    alias(libs.plugins.conventions.compose.library)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "md.vnastasi.shoppinglist.screen.additems.api"
}

dependencies {
    api(libs.navigation.runtime)
    api(libs.serialization.core)
}
