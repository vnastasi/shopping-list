plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "md.vnastasi.shoppinglist.screen.listdetails"
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
        freeCompilerArgs = listOf(
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
            "-opt-in=androidx.compose.ui.ExperimentalComposeUiApi",
            "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
        )
        jvmTarget = "17"
    }
}

dependencies {

    implementation(project(":domain:api"))
    implementation(project(":support:async"))
    implementation(project(":support:lifecycle"))
    implementation(project(":support:ui"))
    implementation(project(":support:theme"))
    implementation(project(":resources"))

    implementation(platform(libs.compose.bom))
    implementation(platform(libs.kotlin.bom))
    implementation(platform(libs.kotlinx.coroutines.bom))
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
    implementation(libs.kotlinx.collections)

    debugImplementation(libs.compose.test.manifest)
    debugImplementation(libs.compose.tooling)

    testImplementation(project(":domain:test-data"))
    testImplementation(project(":support:async-unit-test"))

    testImplementation(platform(libs.kotlinx.coroutines.bom))
    testImplementation(libs.androidx.lificycle.test)
    testImplementation(libs.assertk)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.kotlin.reflect)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.turbine)

    testRuntimeOnly(libs.junit.jupiter.engine)
}
