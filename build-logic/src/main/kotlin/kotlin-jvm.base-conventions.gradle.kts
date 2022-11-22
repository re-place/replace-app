import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

extensions.getByName<KotlinMultiplatformExtension>("kotlin").apply {
    jvm {
        withJava()
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }
}

dependencies {
    "jvmMainImplementation"(kotlin("reflect"))
}
