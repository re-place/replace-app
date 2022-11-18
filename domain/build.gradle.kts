kotlin {
    js(IR) {
        browser {
            binaries.executable()
        }
    }
}

dependencies {
    commonMainImplementation("org.jetbrains.kotlin-wrappers:kotlin-js:1.0.0-pre.439")
}
