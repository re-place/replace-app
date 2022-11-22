plugins {
    id("kotlin-jvm.base-conventions")
    kotlin("plugin.serialization")
}

dependencies {
    jvmMainApi(libs.guice)
    jvmMainImplementation(libs.kotlinx.serialization)
}
