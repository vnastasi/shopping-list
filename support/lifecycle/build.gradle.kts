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

    implementation(platform(libs.kotlin.bom))
    implementation(platform(libs.kotlinx.coroutines.bom))
    implementation(libs.koin.android)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.androidx.core)
    implementation(libs.androidx.lificycle.runtime)
    implementation(libs.androidx.lificycle.viewmodel.compose)
    implementation(libs.androidx.lificycle.runtime.compose)
}
