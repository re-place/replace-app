import com.github.gradle.node.npm.task.NpxTask

@Suppress("DSL_SCOPE_VIOLATION") // https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    id("kotlin-js.base-conventions")
    alias(libs.plugins.node)
}

dependencies {
    commonMainImplementation(project(":domain"))
}

tasks.register<NpxTask>("buildAngularApp") {
    group = "build"
    dependsOn("npmInstall", "jsBrowserDistribution")
    command.set("ng")
    args.set(listOf("build"))
    workingDir.set(File("src/angular"))
    inputs.dir("src/angular")
    outputs.dir("dist")
}
