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
        maven{
            url = uri("https://dl.google.com/dl/android/maven2/com/android/tools/lint/lint-gradle/31.1.0/lint-gradle-31.1.0.jar")
        }
    }
}

rootProject.name = "University"
include(":app")
 