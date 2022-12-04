@Suppress("DSL_SCOPE_VIOLATION") // https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.shadow)
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
    jvmMainImplementation(project(":replace-application"))
    jvmMainImplementation(project(":replace-infrastructure"))
//    commonMainRuntimeOnly(project(":replace-web"))
}

//tasks {
//    register<JavaExec>("runBackendBare") {
//        group = "application"
//        description = "Runs the backend without the frontend"
//        classpath = sourceSets["main"].runtimeClasspath
//    }
//}
