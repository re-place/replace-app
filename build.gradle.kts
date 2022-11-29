@Suppress("DSL_SCOPE_VIOLATION") // https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization) apply false
    id("kotlin-jvm.base-conventions")
    application
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
