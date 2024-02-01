pluginManagement {
    repositories {
        includeBuild("build-logic")
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

rootProject.name = "SkeletonAndroid"
include(":app")
include(":benchmarks")
include(":modules:data")
include(":modules:datastore")
include(":modules:model")
include(":modules:common")
include(":modules:testing")
include(":modules:database")
include(":modules:network")
include(":modules:analytics")
include(":modules:designsystem")
include(":modules:domain")
include(":modules:ui")
include(":features:main")
include(":features:detail")



