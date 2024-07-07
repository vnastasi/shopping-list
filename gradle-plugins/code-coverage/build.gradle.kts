import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-receivers")
    }
}

dependencies {
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.android.gradlePlugin)
    implementation("md.vnastasi.plugins:plugin-support")
}

gradlePlugin {
    plugins {
        register("CodeCoverage") {
            id = "code-coverage"
            implementationClass = "md.vnastasi.plugin.codecoverage.CodeCoveragePlugin"
        }
    }
}
