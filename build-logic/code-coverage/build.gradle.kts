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
    compileOnly(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)

    implementation(project(":plugin-support"))
}

gradlePlugin {
    plugins {
        register("CodeCoverage") {
            id = "code-coverage"
            implementationClass = "md.vnastasi.plugin.codecoverage.CodeCoveragePlugin"
        }
    }
}
