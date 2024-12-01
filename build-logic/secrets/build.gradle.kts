plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.kotlin.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("Secrets") {
            id = "secrets"
            implementationClass = "md.vnastasi.plugin.secrets.SecretsPlugin"
        }
    }
}
