@Suppress("DSL_SCOPE_VIOLATION") // https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    id("kotlin-jvm.base-conventions")
    kotlin("plugin.serialization")
}

dependencies {
    jvmMainImplementation(project(":replace-application"))
    jvmMainImplementation(project(":replace-domain"))
    jvmMainImplementation(libs.kotlinx.coroutines)
    jvmMainImplementation(libs.kotlinx.serialization)
    jvmMainImplementation(libs.ktor.serialization)
    jvmMainImplementation(libs.ktor.server.auth)
    jvmMainImplementation(libs.ktor.server.content.negotiation)
    jvmMainImplementation(libs.ktor.server.cors)
    jvmMainImplementation(libs.ktor.server.netty)
    jvmMainImplementation(libs.ktor.server.resources)
    jvmMainImplementation(libs.ktor.server.sessions)
    jvmMainImplementation(libs.ktor.server.status.pages)
    jvmMainImplementation(libs.tegral.openapi.base)
    jvmMainImplementation(libs.tegral.openapi.swagger)
    jvmTestImplementation(libs.kotest)
}

tasks {
    test {
        useJUnitPlatform()
    }
}
