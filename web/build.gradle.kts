import com.github.gradle.node.npm.task.NpxTask

plugins {
    id("kotlin-js.base-conventions")
    id("com.github.node-gradle.node").version("3.0.1")
}

dependencies {
    commonMainImplementation(project(":domain"))
}

tasks.register<NpxTask>("buildAngularApp") {
    dependsOn("npmInstall")
    command.set("ng")
    args.set(listOf("build", "--prod"))
    inputs.files("package.json", "package-lock.json", "angular.json", "tsconfig.json", "tsconfig.app.json")
    inputs.dir("src")
    inputs.dir(fileTree("node_modules").exclude(".cache"))
    outputs.dir("dist")
}
