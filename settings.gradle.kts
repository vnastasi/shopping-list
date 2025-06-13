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
    ":support:theme",
    ":resources",
    ":screen:shared",
    ":screen:overview",
    ":screen:list-details",
    ":screen:add-items",
    ":app"
)
