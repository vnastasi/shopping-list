pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("com.gradle.develocity") version ("4.2.2")
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

develocity {
    projectId.set("vnastasi/shopping-list")

    buildScan {
        termsOfUseUrl.set("https://gradle.com/help/legal-terms-of-use")
        termsOfUseAgree.set("yes")

        link("VCS", "https://github.com/vnastasi/shopping-list")

        if (System.getenv("CI")?.toBooleanStrictOrNull() == true) {
            tag("GitHub")
            value("Workflow", System.getenv("GITHUB_WORKFLOW"))
            value("Run ID", System.getenv("GITHUB_RUN_ID"))
            value("Run number", System.getenv("GITHUB_RUN_NUMBER"))
            value("Branch", System.getenv("GITHUB_HEAD_REF")?.takeUnless { it.isBlank() } ?: System.getenv("GITHUB_REF_NAME"))
            value("Commit ID", System.getenv("GITHUB_SHA"))
        } else {
            tag("Local")
        }
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "shopping-list"

include(
    ":database",
    ":domain:api",
    ":domain:implementation",
    ":support:annotation",
    ":support:theme",
    ":resources",
    ":screen:shared",
    ":screen:overview",
    ":screen:list-details",
    ":screen:add-items",
    ":app"
)
