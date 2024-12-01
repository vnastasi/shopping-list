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
    compileOnly(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
    compileOnly(platform(libs.kotlin.bom))
    compileOnly(libs.kotlin.stdlib)
    compileOnly(libs.kotlin.gradlePlugin)

    implementation(project(":plugin-support"))
    implementation(libs.detekt.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("DetektAggregator") {
            id = "detekt-aggregator"
            implementationClass = "md.vnastasi.plugin.detektaggregator.DetektAggregatorPlugin"
        }
    }
}
