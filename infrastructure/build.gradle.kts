import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Suppress("DSL_SCOPE_VIOLATION") // https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    id("kotlin-jvm.base-conventions")
    kotlin("plugin.serialization")
    alias(libs.plugins.liquibase)
}

dependencies {
    jvmMainImplementation(project(":replace-application"))
    jvmMainImplementation(project(":replace-domain"))
    jvmMainImplementation(libs.kotlinx.coroutines)
    jvmMainImplementation(libs.kotlinx.serialization)
    jvmMainImplementation(libs.ktor.serialization)
    jvmMainImplementation(libs.ktor.server.auth)
    jvmMainImplementation(libs.ktor.server.content.negotiation)
    jvmMainImplementation(libs.ktor.server.cors)
    jvmMainImplementation(libs.ktor.server.netty)
    jvmMainImplementation(libs.ktor.server.resources)
    jvmMainImplementation(libs.ktor.server.sessions)
    jvmMainImplementation(libs.ktor.server.status.pages)
    jvmMainImplementation(libs.tegral.openapi.base)
    jvmMainImplementation(libs.tegral.openapi.swagger)
    jvmTestImplementation(libs.kotest)
    liquibaseRuntime("org.liquibase:liquibase-core:4.18.0")
    liquibaseRuntime("info.picocli:picocli:4.7.0")
    liquibaseRuntime("org.liquibase:liquibase-groovy-dsl:3.0.2")
    liquibaseRuntime("org.postgresql:postgresql:42.5.1")
}

apply(plugin = "liquibase")

tasks {
    test {
        useJUnitPlatform()
    }
}

val migrationDir = File("infrastructure/src/jvmMain/resources/db/migrations")
val migrationRoot = File("infrastructure/src/jvmMain/resources/db/changelog-root.json")

liquibase {
    activities {
        register("main") {
            val dbUrl = "jdbc:postgresql://localhost:5432/replace-app" // by project.extra.properties
            val dbUser = "postgres" // by project.extra.properties
            val dbPass = "postgres" // by project.extra.properties

            this.arguments = mapOf(
                "logLevel" to "info",
                "changeLogFile" to migrationRoot.path,
                "url" to dbUrl,
                "username" to dbUser,
                "password" to dbPass
            )
        }
    }

    runList = "main"
}

val migrationStub = File("infrastructure/src/jvmMain/resources/db/changelog-stub.json")

tasks {
    register("make-migration") {
        val migrationName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"))
        val migrationFile = File("${migrationDir.path}/$migrationName.json")

        migrationStub.copyTo(migrationFile)

        doLast {
            println("Created migration file: ${migrationFile.path}")

            migrationStub.readText().replace("{{id}}", migrationName).also {
                migrationFile.writeText(it)
            }
        }
    }
}
