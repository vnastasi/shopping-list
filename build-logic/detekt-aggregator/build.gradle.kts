plugins {
    `kotlin-dsl`
}

dependencies {
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

        register("ApplyDetektAggregator") {
            id = "apply-detekt-aggregator"
            implementationClass = "md.vnastasi.plugin.detektaggregator.ApplyDetektAggregatorPlugin"
        }
    }
}
