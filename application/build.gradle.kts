plugins {
    application
    id("kotlin-jvm.base-conventions")
}

application {
    mainClass.set("replace.MainKt")
}

dependencies {
    commonMainImplementation(project(":domain"))
    commonMainRuntimeOnly(project(":infrastructure"))
}
