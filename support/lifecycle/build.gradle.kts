plugins {
    id("compose-library.conventions")
}

android {
    namespace = "md.vnastasi.shoppinglist.support.lifecycle"
}

dependencies {

    implementation(libs.androidx.core)
    implementation(libs.androidx.lificycle.viewmodel.compose)
}
