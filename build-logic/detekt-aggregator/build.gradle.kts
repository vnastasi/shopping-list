plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
    compileOnly(platform(libs.kotlin.bom))
    compileOnly(libs.kotlin.stdlib)
    compileOnly(libs.kotlin.gradlePlugin)

    implementation(project(":plugin-support"))
    implementation(libs.detekt2.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("DetektAggregator") {
            id = "detekt-aggregator"
            implementationClass = "md.vnastasi.plugin.detektaggregator.DetektAggregatorPlugin"
        }
    }
}
