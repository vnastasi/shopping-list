plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "md.vnastasi.shoppinglist.res"
    compileSdk = 34

    defaultConfig {
        minSdk = 28
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
