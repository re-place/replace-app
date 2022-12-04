plugins {
    id("kotlin-js.base-conventions")
    id("kotlin-jvm.base-conventions")
    kotlin("plugin.serialization")
}

dependencies {
    jvmMainApi(libs.bundles.kmongo)
    jvmTestImplementation(libs.kotest)
}
