pluginManagement {
    includeBuild("build-logic")
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
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}



rootProject.name = "BeePadel"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":app")
include(":match_history:data")
include(":match_history:domain")
include(":match_history:presentation")
include(":core:domain")
include(":core:data")
include(":core:presentation")
include(":active_match:data")
include(":active_match:domain")
include(":active_match:presentation")
include(":core:presentation:designsystem")
include(":core:presentation:ui")
include(":core:database")
