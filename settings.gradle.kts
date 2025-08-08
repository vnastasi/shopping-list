pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
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
