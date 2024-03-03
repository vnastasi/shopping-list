plugins {
    id("simple-library.conventions")
}

android {
    namespace = "md.vnastasi.shoppinglist.db.test"
}

dependencies {

    implementation(project(":database:implementation"))
}
