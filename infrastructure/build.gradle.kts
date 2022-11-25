@Suppress("DSL_SCOPE_VIOLATION") // https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.kotlin.spring)
    id("kotlin-jvm.base-conventions")
}

dependencies {
    commonMainImplementation(project(":replace-application"))
    commonMainImplementation(project(":replace-domain"))
    jvmMainImplementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    jvmMainImplementation("org.springframework.boot:spring-boot-starter-data-rest")
    jvmMainImplementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    jvmMainImplementation("org.springframework.boot:spring-boot-starter-security")
    jvmMainImplementation("org.springframework.boot:spring-boot-starter-web")
    jvmMainImplementation("org.springframework.boot:spring-boot-starter-web-services")
    jvmMainImplementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    jvmMainImplementation("org.springframework.session:spring-session-core")
    jvmTestImplementation("org.springframework.boot:spring-boot-starter-test")
    jvmTestImplementation("org.springframework.security:spring-security-test")
}
