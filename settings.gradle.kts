@file:Suppress("UnstableApiUsage")

dependencyResolutionManagement {
//    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven("https://repo.spring.io/milestone")
        maven("https://repo.spring.io/snapshot")
    }
}

pluginManagement {
    includeBuild("build-logic")
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://repo.spring.io/milestone")
        maven("https://repo.spring.io/snapshot")
    }
}

rootProject.name = "replace-app"

sequenceOf(
    "api",
    "application",
    "domain",
    "infrastructure",
    "web",
).forEach {
    val project = ":replace-$it"
    include(project)
    project(project).projectDir = file(it)
}
