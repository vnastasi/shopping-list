plugins {
    id("compose-screen-library.conventions")
}

android {
    namespace = "md.vnastasi.shoppinglist.screen.additems"
}

dependencies {

    testImplementation(libs.androidx.lificycle.test)
}
