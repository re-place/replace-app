@file:Suppress("UnstableApiUsage")

dependencyResolutionManagement {
//    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
    }
}

pluginManagement {
    includeBuild("build-logic")
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "replace-app"

sequenceOf(
    "application",
    "domain",
    "infrastructure",
).forEach {
    val project = ":replace-$it"
    include(project)
    project(project).projectDir = file(it)
}
