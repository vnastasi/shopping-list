pluginManagement {
    includeBuild("gradle-plugins/app-build-support")
    includeBuild("gradle-plugins/conventions")
    includeBuild("gradle-plugins/detekt-aggregator")
    includeBuild("gradle-plugins/code-coverage")
    includeBuild("gradle-plugins/plugin-support")
    includeBuild("gradle-plugins/secrets")
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
    ":database:implementation",
    ":domain:api",
    ":domain:implementation",
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
