// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.cacheFix).apply(false)
    alias(libs.plugins.android.library).apply(false)
    alias(libs.plugins.android.application).apply(false)
    alias(libs.plugins.compose.compiler).apply(false)
    alias(libs.plugins.hilt).apply(false)
    alias(libs.plugins.kotlin.android).apply(false)
    alias(libs.plugins.kotlin.jvm).apply(false)
    alias(libs.plugins.kotlin.parcelize).apply(false)
    alias(libs.plugins.ksp).apply(false)
    alias(libs.plugins.paparazzi).apply(false)
    alias(libs.plugins.gradle.dependencies)
    id("code-coverage")
    id("detekt-aggregator")
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
    coverageThreshold.set(providers.environmentVariable("CODE_COVERAGE_THRESHOLD").map { it.toBigDecimal() }.orElse(80.toBigDecimal()))
}

dependencyAnalysis {
    issues {
       all {
           onUnusedDependencies {
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

tasks.register<Delete>("clean").configure {
    delete.add(project.layout.buildDirectory)
}
