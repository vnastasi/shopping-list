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
    implementation(platform(libs.kotlin.bom))
    implementation(libs.kotlin.stdlib)

    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.android.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("JacocoAggregator") {
            id = "jacoco-aggregator"
            implementationClass = "md.vnastasi.plugin.detektaggregator.JacocoAggregatorPlugin"
        }
    }
}
