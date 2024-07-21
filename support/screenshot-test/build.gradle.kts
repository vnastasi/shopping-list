plugins {
    id("simple-library.conventions")
}

android {
    namespace = "md.vnastasi.shoppinglist.support.screenshot.test"
}

dependencies {
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.test.junit4)
    implementation(libs.paparazzi)
}
