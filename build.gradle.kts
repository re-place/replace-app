@Suppress("DSL_SCOPE_VIOLATION") // https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    alias(libs.plugins.kotlin.multiplatform)
}

allprojects {
    apply(plugin = "org.jetbrains.kotlin.multiplatform")
    kotlin {
        jvm()
        js(IR)
    }
    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }
}
