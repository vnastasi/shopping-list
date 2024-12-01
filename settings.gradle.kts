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

rootProject.name = "Shopping list"

include(
    ":database",
    ":domain:api",
    ":domain:implementation",
    ":support:annotation",
    ":support:async",
    ":support:async-unit-test",
    ":support:lifecycle",
    ":support:ui",
    ":support:theme",
    ":resources",
    ":screen:overview",
    ":screen:list-details",
    ":screen:add-items",
    ":app"
)
