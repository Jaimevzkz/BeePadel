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
include(":match:data")
include(":match:domain")
include(":match:presentation")
include(":core:domain")
include(":core:data")
include(":core:presentation")
include(":core:presentation:designsystem")
include(":core:presentation:ui")
include(":core:database:data")
include(":core:database:domain")
include(":common:sharedTest")
include(":common:general")
include(":wear:app")
include(":wear:match:data")
include(":wear:match:domain")
include(":wear:match:presentation")
include(":core:presentation:designsystem_wear")
