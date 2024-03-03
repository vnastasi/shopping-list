plugins {
    id("simple-library.conventions")
}

android {
    namespace = "md.vnastasi.shoppinglist.support.async.test"
}

dependencies {

    implementation(project(":support:async"))
    implementation(platform(libs.kotlinx.coroutines.bom))
    implementation(libs.kotlinx.coroutines.core)
}
