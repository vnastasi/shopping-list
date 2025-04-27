// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.cacheFix).apply(false)
    alias(libs.plugins.android.library).apply(false)
    alias(libs.plugins.android.application).apply(false)
    alias(libs.plugins.compose.compiler).apply(false)
    alias(libs.plugins.kotlin.android).apply(false)
    alias(libs.plugins.kotlin.parcelize).apply(false)
    alias(libs.plugins.ksp).apply(false)
    alias(libs.plugins.paparazzi).apply(false)
    alias(libs.plugins.gradle.wrapper.upgrade)
    id("code-coverage")
    id("detekt-aggregator")
}

codeCoverage {
    targetBuildType.set("debug")
    reportDirectory.set(layout.buildDirectory.dir("reports/code-coverage"))
    exclusions.add("**/*Preview*")
    coverageThreshold.set(0.90)
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