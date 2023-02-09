import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

extensions.getByName<KotlinMultiplatformExtension>("kotlin").apply {
    jvm {
        withJava()
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
                freeCompilerArgs = listOf("-Xcontext-receivers")
            }
        }
    }
}

extensions.getByName<JavaPluginExtension>("java").apply {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    "jvmMainImplementation"(kotlin("reflect"))
}
