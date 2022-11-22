@Suppress("DSL_SCOPE_VIOLATION") // https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    alias(libs.plugins.kotlin.multiplatform)
}

kotlin {
    // TODO: Figure out how to only define common target
    jvm()
}

allprojects {
    apply(plugin = "org.jetbrains.kotlin.multiplatform")
}
