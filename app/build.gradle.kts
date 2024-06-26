import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("application.conventions")
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.ksp)
}

android {
    namespace = "md.vnastasi.shoppinglist"

    defaultConfig {
        applicationId = libs.versions.project.application.id.get()
        targetSdk = libs.versions.project.targetSdk.get().toInt()
        versionCode = libs.versions.project.version.code.get().toInt()
        versionName = libs.versions.project.version.name.get()

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

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    sourceSets {
        getByName("androidTest") {
            assets {
                srcDir("${project(":database:implementation").layout.projectDirectory}/schemas")
            }
        }
    }
}

dependencies {

    implementation(project(":database:implementation"))
    implementation(project(":domain:api"))
    implementation(project(":domain:implementation"))
    implementation(project(":resources"))
    implementation(project(":screen:add-items"))
    implementation(project(":screen:list-details"))
    implementation(project(":screen:overview"))
    implementation(project(":support:async"))
    implementation(project(":support:lifecycle"))
    implementation(project(":support:theme"))
    implementation(project(":support:ui"))

    implementation(platform(libs.compose.bom))
    implementation(platform(libs.kotlin.bom))
    implementation(platform(libs.kotlinx.coroutines.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core)
    implementation(libs.androidx.lificycle.runtime)
    implementation(libs.androidx.lificycle.runtime.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.koin.android)
    implementation(libs.koin.android.compose)

    testImplementation(project(":domain:test-data"))
    testImplementation(project(":support:async-unit-test"))

    testImplementation(libs.androidx.lificycle.test)

    androidTestImplementation(project(":database:test-data"))

    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.assertk)
    androidTestImplementation(libs.compose.test.junit4)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.espresso.idling.concurrent)
    androidTestImplementation(libs.kotlin.reflect)
    androidTestImplementation(libs.room.test)
    androidTestImplementation(libs.turbine)
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        freeCompilerArgs.add("-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi")
    }
}
