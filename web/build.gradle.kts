import com.github.gradle.node.npm.task.NpxTask

@Suppress("DSL_SCOPE_VIOLATION") // https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    id("kotlin-js.base-conventions")
    id("com.github.johnrengelman.shadow")
    alias(libs.plugins.node)
}

dependencies {
    commonMainImplementation(project(":replace-domain"))
}

tasks.register<NpxTask>("buildAngularApp") {
    group = "build"
    dependsOn("npmInstall", "jsBrowserDistribution")
    command.set("ng")
    args.set(listOf("build"))
    workingDir.set(File("src/angular"))
    inputs.dir("src/angular")
    outputs.dir("build/angular")
}

tasks.withType<Jar> {
    dependsOn("buildAngularApp")
    from("build/angular").into("static")
}
