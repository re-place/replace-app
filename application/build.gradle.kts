plugins {
    application
}

application {
    mainClass.set("replace.MainKt")
}

kotlin {
    jvm()
}

dependencies {
    jvmMainImplementation(kotlin("reflect"))
    commonMainImplementation(project(":domain"))
    commonMainRuntimeOnly(project(":infrastructure"))
}
