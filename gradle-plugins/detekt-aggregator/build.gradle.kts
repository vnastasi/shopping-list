import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + setOf(
            "-Xcontext-receivers"
        )
    }
}

dependencies {
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
    implementation(platform(libs.kotlin.bom))
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.detekt.gradlePlugin)
    implementation("md.vnastasi.plugins:plugin-support")
}

gradlePlugin {
    plugins {
        register("DetektAggregator") {
            id = "detekt-aggregator"
            implementationClass = "md.vnastasi.plugin.detektaggregator.DetektAggregatorPlugin"
        }
    }
}
