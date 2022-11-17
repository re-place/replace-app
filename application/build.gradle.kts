plugins {
    application
}

application {
    mainClass.set("replace.MainKt")
}

dependencies {
    implementation(project(":domain"))
    runtimeOnly(project(":infrastructure"))
}
