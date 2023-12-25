plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "md.vnastasi.shoppinglist.support.lifecycle"
    compileSdk = 34

    defaultConfig {
        minSdk = 28
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation(libs.androidx.core)
    implementation(libs.androidx.lificycle.runtime)
    implementation(libs.androidx.lificycle.runtime.compose)
    implementation(libs.androidx.lificycle.viewmodel.compose)
}
