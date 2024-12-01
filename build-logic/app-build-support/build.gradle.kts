plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.android.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("AppBuildSupport") {
            id = "app-build-support"
            implementationClass = "md.vnastasi.plugin.abs.AppBuildSupportPlugin"
        }
    }
}

