@Suppress("DSL_SCOPE_VIOLATION") // https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    application
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    id("kotlin-jvm.base-conventions")
    kotlin("plugin.serialization")
}

application {
    mainClass.set("replace.MainKt")
}

dependencies {
    commonMainImplementation(project(":replace-api"))
    commonMainImplementation(project(":replace-domain"))
    commonMainRuntimeOnly(project(":replace-infrastructure"))
    jvmMainImplementation(libs.kotlinx.serialization)
}
