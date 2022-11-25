@Suppress("DSL_SCOPE_VIOLATION") // https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization) apply false
    id("kotlin-jvm.base-conventions")
    application

    // spring plugins that technically shouldn't be needed in this project
    // because all the spring code is in infrastructure, but they are needed
    // to make it work (tm)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

allprojects {
    apply(plugin = "org.jetbrains.kotlin.multiplatform")
}

application {
    mainClass.set("replace.MainKt")
}

dependencies {
    commonMainImplementation(project(":replace-application"))
    commonMainRuntimeOnly(project(":replace-infrastructure"))
}
