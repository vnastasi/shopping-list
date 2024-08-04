plugins {
    id("simple-library.conventions")
    alias(libs.plugins.ksp)
}

android {
    namespace = "md.vnastasi.shoppinglist.db.impl"

    ksp {
        arg(roomSchemaDir(file("${layout.projectDirectory}/schemas")))
    }
}

dependencies {

    implementation(platform(libs.kotlin.bom))
    implementation(platform(libs.kotlinx.coroutines.bom))
    implementation(libs.koin.android)
    implementation(libs.room)
    implementation(libs.room.runtime)

    ksp(libs.room.compiler)
}
