plugins {
    id("compose-screen-library.conventions")
}

android {
    namespace = "md.vnastasi.shoppinglist.screen.listdetails"
}

dependencies {

    testImplementation(libs.androidx.lificycle.test)
}
