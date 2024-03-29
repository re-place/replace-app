@Suppress("DSL_SCOPE_VIOLATION") // https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ktlint)
    id("kotlin-jvm.base-conventions")
    application
}

val projectVersion = file("version").readLines().first()

allprojects {
    version = projectVersion
    apply(plugin = "org.jetbrains.kotlin.multiplatform")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
}

dependencies {
    jvmMainImplementation(project(":replace-application"))
    jvmMainImplementation(project(":replace-infrastructure"))
}

tasks {
    val runDir = File("build/run")
    withType<JavaExec> {
        doFirst {
            runDir.mkdirs()
        }
        workingDir = runDir
    }
}
