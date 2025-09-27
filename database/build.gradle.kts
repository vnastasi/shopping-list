plugins {
    id("simple-library.conventions")
    alias(libs.plugins.ksp)
}

android {
    namespace = "md.vnastasi.shoppinglist.db"
    
    buildFeatures {
        buildConfig = true
    }
}

ksp {
    arg("room.schemaLocation", "${layout.projectDirectory}/schemas")
}

dependencies {
    implementation(platform(libs.coroutines.bom))
    implementation(platform(libs.kotlin.bom))

    api(libs.coroutines.core)
    api(libs.koin.core)
    api(libs.sqlite)

    implementation(libs.koin.android)
    implementation(libs.room.common)
    implementation(libs.room.runtime)

    ksp(libs.room.compiler)
}
