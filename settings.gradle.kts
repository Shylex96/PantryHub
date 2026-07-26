pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "PantryHub"
include(":app")

// Core modules
include(":core-common")
include(":core-database")
include(":core-designsystem")
include(":core-model")
include(":core-navigation")

// Feature modules
include(":feature-shopping")
include(":feature-products")
include(":feature-notes")
include(":feature-settings")
include(":feature-importexport")
