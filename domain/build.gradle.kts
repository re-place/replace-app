plugins {
    id("kotlin-js.base-conventions")
    id("kotlin-jvm.base-conventions")
    kotlin("plugin.serialization")
}

dependencies {
    jvmMainImplementation(libs.ktor.server.auth)
    jvmTestImplementation(libs.kotest)
    jvmMainImplementation(libs.exposed.core)
    jvmMainImplementation(libs.exposed.dao)
    jvmMainImplementation(libs.exposed.jdbc)
    jvmMainImplementation(libs.exposed.java.time)
    jvmMainImplementation(libs.kotlinx.datetime)
}

tasks {
    @Suppress("UnstableApiUsage")
    withType<ProcessResources> {
        from(rootProject.file("version")) {
            into("replace/")
        }
    }
}
