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
    implementation(platform(libs.coroutines.bom))

    api(libs.sqlite)
    api(libs.koin.core)
    api(libs.coroutines.core)

    implementation(libs.koin.android)
    implementation(libs.room.common)
    implementation(libs.room.runtime)

    ksp(libs.room.compiler)
}
