@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "RSS Reader"
include(":app")
include(":navigation")
include(":data")

include(":core:ui")
include(":core:utils")

include(":feature:feed")
include(":feature:search:common")
include(":feature:search:impl")
include(":feature:article_viewer")


