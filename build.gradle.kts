// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.cacheFix).apply(false)
    alias(libs.plugins.android.library).apply(false)
    alias(libs.plugins.android.application).apply(false)
    alias(libs.plugins.compose.compiler).apply(false)
    alias(libs.plugins.kotlin.android).apply(false)
    alias(libs.plugins.kotlin.jvm).apply(false)
    alias(libs.plugins.kotlin.parcelize).apply(false)
    alias(libs.plugins.ksp).apply(false)
    alias(libs.plugins.paparazzi).apply(false)
    alias(libs.plugins.gradle.dependencies)
    alias(libs.plugins.gradle.wrapper.upgrade)
    id("code-coverage")
    id("detekt-aggregator")
}

codeCoverage {
    targetBuildType.set("debug")
    reportDirectory.set(layout.buildDirectory.dir("reports/code-coverage"))
    excludedModules.addAll(projects.support.annotation, projects.support.async, projects.support.asyncUnitTest)
    excludedClasses.addAll("**/*Preview*", "**/*Database_Impl.**", "**/*Database_Impl$*$*$1.**", "**/Routes$**")
    coverageThreshold.set(0.88)
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
               // Let Paparazzi configure these
               exclude(
                   "com.android.tools.layoutlib:layoutlib-api",
                   "com.android.tools.layoutlib:layoutlib",
                   "junit:junit"
               )
               severity("warn")
           }
       }
    }
}

wrapperUpgrade {
    gradle {
        register("root") {
            repo.set("vnastasi/shopping-list")
            baseBranch.set("main")
            options {
                labels.add("dependencies")
                reviewers.add("vnastasi")
                recreateClosedPullRequest.set(true)
            }
        }
    }
}