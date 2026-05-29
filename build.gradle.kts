plugins {
    alias(libs.plugins.conventions.code.coverage)
    alias(libs.plugins.conventions.detekt.aggregator)
    alias(libs.plugins.gradle.dependency.analysis)
    alias(libs.plugins.gradle.dependency.sort)

    alias(libs.plugins.android.cacheFix) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.compose.screenshot) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.ksp) apply false
}

codeCoverage {
    targetBuildType.set("debug")
    reportDirectory.set(layout.buildDirectory.dir("reports/code-coverage"))
    excludedModules.addAll(project(":support:annotation"))
    excludedClasses.addAll(
        "**/*Preview*",
        "**/*Database_Impl.**",
        "**/*Database_Impl$*$*$1.**",
        "**/ShoppingListApplication.**",
        "**/MainActivity.**"
    )
    coverageThreshold.set(providers.environmentVariable("CODE_COVERAGE_THRESHOLD").map { it.toBigDecimal() }.orElse(89.toBigDecimal()))
}

dependencyAnalysis {
    issues {
        all {
            onUnusedDependencies {
                exclude(
                    "org.jetbrains.kotlin:kotlin-stdlib",
                    ":domain:api" // Plugin has issues with screenshotTest configuration and flags test fixtures as not used
                )
                severity("fail")
            }

            onRedundantPlugins {
                severity("fail")
            }

            onUnusedAnnotationProcessors {
                severity("fail")
            }

            onUsedTransitiveDependencies {
                exclude(
                    "androidx.fragment:fragment",
                    "com.android.tools.layoutlib:layoutlib-api",
                    "com.android.tools.layoutlib:layoutlib",
                    "junit:junit"
                )
                severity("warn")
            }
        }
    }
}

sortDependencies {
    insertBlankLines.set(true)
}

tasks.register<Delete>("clean").configure {
    delete.add(project.layout.buildDirectory)
}
