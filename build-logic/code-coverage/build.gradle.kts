plugins {
    `kotlin-dsl`
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
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
