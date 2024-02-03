plugins {
    id("simple-library.conventions")
}

android {
    namespace = "md.vnastasi.shoppinglist.domain"
}

dependencies {

    implementation(project(":domain:api"))
}
