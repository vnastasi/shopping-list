import md.vnastasi.plugin.support.libs

plugins {
    id("simple-library.conventions")
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "md.vnastasi.shoppinglist.db"
}

ksp {
    arg("room.schemaLocation", "${layout.projectDirectory}/schemas")
}

dependencies {
    api(libs.coroutines.core)
    api(libs.dagger)
    api(libs.hilt.android)
    api(libs.javax.inject)
    api(libs.sqlite)

    implementation(platform(libs.coroutines.bom))
    implementation(platform(libs.kotlin.bom))
    implementation(libs.hilt.core)
    implementation(libs.room.common)
    implementation(libs.room.runtime)

    ksp(libs.bundles.hilt.compiler)
    ksp(libs.room.compiler)
}
