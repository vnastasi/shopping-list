plugins {
    id("simple-library.conventions")
}

android {
    namespace = "md.vnastasi.shoppinglist.domain.test"
}

dependencies {

    implementation(project(":domain:api"))
}
