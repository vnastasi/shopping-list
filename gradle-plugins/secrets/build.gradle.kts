plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.kotlin.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("Screts") {
            id = "secrets"
            implementationClass = "md.vnastasi.plugin.secrets.SecretsPlugin"
        }
    }
}
