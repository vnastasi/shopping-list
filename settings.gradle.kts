pluginManagement {
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
    ":database:test-data",
    ":domain:api",
    ":domain:implementation",
    ":domain:test-data",
    ":support:async",
    ":support:async-unit-test",
    ":support:lifecycle",
    ":app"
)
