plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("AppBuildSupport") {
            id = "app-build-support"
            implementationClass = "md.vnastasi.plugin.abs.AppBuildSupportPlugin"
        }
    }
}

