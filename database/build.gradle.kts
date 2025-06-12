plugins {
    id("simple-library.conventions")
    alias(libs.plugins.ksp)
}

android {
    namespace = "md.vnastasi.shoppinglist.db"
}

ksp {
    arg("room.schemaLocation", "${layout.projectDirectory}/schemas")
}

dependencies {
    implementation(platform(libs.kotlin.bom))
    implementation(platform(libs.kotlinx.coroutines.bom))

    api(libs.androidx.sqlite)
    api(libs.koin.core)
    api(libs.kotlinx.coroutines.core)

    implementation(libs.koin.android)
    implementation(libs.room.common)
    implementation(libs.room.runtime)

    ksp(libs.room.compiler)
}
