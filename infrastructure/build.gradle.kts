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
    liquibaseRuntime(libs.liquibase.core)
    liquibaseRuntime(libs.liquibase.picocli)
    liquibaseRuntime(libs.postgrsql)
    jvmMainImplementation(libs.exposed.core)
    jvmMainImplementation(libs.exposed.dao)
    jvmMainImplementation(libs.exposed.jdbc)
    jvmMainImplementation(libs.exposed.java.time)
    jvmMainImplementation(libs.postgrsql)
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
        doLast {
            val migrationPrefix = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"))
            val migrationName = "${migrationPrefix}_${project.findProperty("id") ?: ""}"
            val migrationFile = File("${migrationDir.path}/$migrationName.json")

            migrationStub.copyTo(migrationFile)

            println("Created migration file: ${migrationFile.path}")

            val newText = migrationStub.readText().replace("{{id}}", migrationName)
            migrationFile.writeText(newText)
        }
    }
    register("fresh", ) {
        dependsOn("dropAll")
        dependsOn("update")
    }
}
