import com.typesafe.config.ConfigFactory
import io.ktor.plugin.features.JreVersion
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

buildscript {
    dependencies {
        classpath("com.typesafe:config:1.4.2")
    }
}

@Suppress("DSL_SCOPE_VIOLATION") // https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    id("kotlin-jvm.base-conventions")
    kotlin("plugin.serialization")
    alias(libs.plugins.liquibase)
    alias(libs.plugins.ktor)
}

dependencies {
    implementation("io.ktor:ktor-client-content-negotiation:2.2.2")
    implementation("io.ktor:ktor-serialization-gson:2.2.2")
    jvmMainImplementation(project(":replace-application"))
    jvmMainImplementation(project(":replace-domain"))
    jvmMainImplementation(libs.kotlinx.coroutines)
    jvmMainImplementation(libs.kotlinx.serialization)
    jvmMainImplementation(libs.ktor.client.cio)
    jvmMainImplementation(libs.ktor.client.content.negotiation)
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
    liquibaseRuntime(libs.liquibase.core)
    liquibaseRuntime(libs.liquibase.picocli)
    liquibaseRuntime(libs.postgrsql)
    jvmMainImplementation(libs.exposed.core)
    jvmMainImplementation(libs.exposed.dao)
    jvmMainImplementation(libs.exposed.jdbc)
    jvmMainImplementation(libs.exposed.java.time)
    jvmMainImplementation(libs.postgrsql)
    jvmMainImplementation(libs.kotlinx.datetime)
}

application {
    mainClass.set("replace.MainKt")
}

ktor {
    docker {
        jreVersion.set(JreVersion.JRE_17)
        project.findProperty("ktorImage")?.also {
            localImageName.set(it.toString())
        }
        project.findProperty("ktorTag")?.also {
            imageTag.set(it.toString())
        }
    }
}

tasks {
    withType<JavaExec> {
        doFirst {
            val runDir = file("build/run")
            runDir.mkdirs()
            workingDir = runDir
        }
    }
    test {
        useJUnitPlatform()
    }
}

val migrationDir = File("infrastructure/src/jvmMain/resources/db/migrations")
val migrationRoot = File("infrastructure/src/jvmMain/resources/db/changelog-root.json")
val configFile = File("infrastructure/src/jvmMain/resources/application.conf")

liquibase {
    activities {
        register("main") {

            if (!configFile.exists()) {
                throw IllegalArgumentException("Config file does not exist")
            }

            val config =
                ConfigFactory.parseString(File("infrastructure/src/jvmMain/resources/application.conf").readText())

            val dbUrl = config.getString("ktor.db.url") ?: throw IllegalArgumentException("Database URL not set")
            val dbUser = config.getString("ktor.db.user") ?: ""
            val dbPass = config.getString("ktor.db.password") ?: ""

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
    register("fresh") {
        dependsOn("dropAll")
        dependsOn("update")
    }
}
