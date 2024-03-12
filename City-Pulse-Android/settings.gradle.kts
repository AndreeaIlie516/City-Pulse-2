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
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "CityPulse"
include(":app")
include(":core")
include(":feature_event")
include(":feature_event:event_data")
include(":feature_event:event_domain")
include(":feature_event:event_presentation")
include(":feature_user")
include(":feature_user:user_data")
include(":feature_user:user_presentation")
include(":feature_user:user_domain")
include(":feature_artist")
include(":feature_city")
include(":feature_genre")
include(":feature_location")
include(":feature_artist:sartist_data")
include(":feature_artist:artist_data")
include(":feature_artist:artist_domain")
include(":feature_city:city_data")
include(":feature_city:city_domain")
include(":feature_genre:genre_data")
include(":feature_genre:genre_domain")
include(":feature_location:location_data")
include(":feature_location:location_domain")
