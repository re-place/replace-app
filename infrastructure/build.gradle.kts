@Suppress("DSL_SCOPE_VIOLATION") // https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    id("kotlin-jvm.base-conventions")
}

dependencies {
    implementation("io.ktor:ktor-server-content-negotiation-jvm:2.1.3")
    implementation("io.ktor:ktor-server-core-jvm:2.1.3")
    implementation("io.ktor:ktor-serialization-gson-jvm:2.1.3")
    commonMainImplementation(project(":replace-application"))
    commonMainImplementation(project(":replace-domain"))
    jvmMainImplementation(libs.kotlinx.coroutines)
    jvmMainImplementation(libs.kotlinx.serialization)
    jvmMainImplementation(libs.ktor.serialization)
    jvmMainImplementation(libs.ktor.server.auth)
    jvmMainImplementation(libs.ktor.server.content.negotiation)
    jvmMainImplementation(libs.ktor.server.cors)
    jvmMainImplementation(libs.ktor.server.netty)
    jvmMainImplementation(libs.ktor.server.sessions)
    jvmMainImplementation(libs.ktor.server.status.pages)
}
