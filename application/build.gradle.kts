@Suppress("DSL_SCOPE_VIOLATION") // https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    id("kotlin-jvm.base-conventions")
    kotlin("plugin.serialization")
}

dependencies {
    commonMainImplementation(project(":replace-domain"))
    jvmMainApi(libs.guice)
    jvmMainApi(libs.logging.api)
    jvmMainImplementation(libs.logging.impl)
    jvmMainImplementation(libs.logging.core)
    jvmMainImplementation(libs.kotlinx.serialization)
    jvmMainImplementation(libs.exposed.core)
    jvmMainImplementation(libs.exposed.dao)
    jvmMainImplementation(libs.exposed.jdbc)
    jvmMainImplementation(libs.exposed.java.time)
    jvmMainImplementation(libs.kotlinx.datetime)
    jvmTestImplementation(libs.kotest.core)
    jvmTestImplementation(libs.kotest.property)
    jvmTestImplementation("org.testcontainers:postgresql:1.17.6")
    jvmTestImplementation(libs.postgrsql)
}

tasks {
    withType<Test> {
        useJUnitPlatform()
    }
}
