plugins {
    id("simple-library.conventions")
}

android {
    namespace = "md.vnastasi.shoppinglist.db"
}

dependencies {

    implementation(project(":database:implementation"))
}
