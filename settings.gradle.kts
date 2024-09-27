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
        mavenLocal()
//         jcenter()
        maven { url = uri("https://jitpack.io") }
        gradlePluginPortal()

    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        mavenLocal()
//         jcenter()
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "BMI SDK"
include(":app")
