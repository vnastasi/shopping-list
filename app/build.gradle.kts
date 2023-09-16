plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.ksp)
}

android {
    namespace = "md.vnastasi.shoppinglist"
    compileSdk = 34

    defaultConfig {
        applicationId = "md.vnastasi.shoppinglist"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.kotlinCompilerExt.get()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        freeCompilerArgs = listOf(
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
            "-opt-in=androidx.compose.ui.ExperimentalComposeUiApi",
            "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
        )
        jvmTarget = "17"
    }

    ksp {
        arg(roomSchemaDir(file("${layout.projectDirectory}/schemas")))
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    sourceSets {
        getByName("androidTest") {
            assets {
                srcDir("${layout.projectDirectory}/schemas")
            }
        }
    }
}

dependencies {

    implementation(platform(libs.compose.bom))
    implementation(platform(libs.kotlin.bom))
    implementation(platform(libs.kotlin.coroutines.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core)
    implementation(libs.androidx.lificycle.runtime)
    implementation(libs.androidx.lificycle.runtime.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.compose.graphics)
    implementation(libs.compose.material)
    implementation(libs.compose.preview)
    implementation(libs.compose.ui)
    implementation(libs.koin.android)
    implementation(libs.koin.android.compose)
    implementation(libs.kotlin.collections)
    implementation(libs.room)
    implementation(libs.room.runtime)

    debugImplementation(libs.compose.test.manifest)
    debugImplementation(libs.compose.tooling)

    ksp(libs.room.compiler)

    testImplementation(platform(libs.kotlin.coroutines.bom))
    testImplementation(libs.androidx.lificycle.test)
    testImplementation(libs.assertk)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.turbine)

    testRuntimeOnly(libs.junit.jupiter.engine)

    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.compose.test.junit4)
    androidTestImplementation(libs.espresso.core)
}
