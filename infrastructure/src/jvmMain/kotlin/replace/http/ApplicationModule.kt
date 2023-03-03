package replace.http

import guru.zoroark.tegral.openapi.ktor.TegralOpenApiKtor
import guru.zoroark.tegral.openapi.ktor.openApiEndpoint
import guru.zoroark.tegral.openapi.ktorui.TegralSwaggerUiKtor
import guru.zoroark.tegral.openapi.ktorui.swaggerUiEndpoint
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.config.tryGetString
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.resources.Resources
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.DatabaseConfig
import replace.Replace
import replace.datastore.DBFileStorage
import replace.job.DeleteOldTemporaryFileUploadsJob

fun Application.applicationModule() {
    println("Starting backend...")
    val environment = environment
    install(CORS) {
        if (environment.developmentMode) {
            anyHost()
        }

        allowHeader(HttpHeaders.ContentType)
        exposeHeader(HttpHeaders.ContentType)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Put)
    }
    install(ContentNegotiation) {
        json(
            Json {
                encodeDefaults = false
                ignoreUnknownKeys = true
            }
        )
    }
    // install type-safe routing
    install(Resources)

    install(TegralOpenApiKtor) {
        title = "Replace API"
        description = "API for the Replace application"
        version = Replace.version
    }

    install(TegralSwaggerUiKtor)

    val databaseConfig = DatabaseConfig {
        keepLoadedReferencesOutOfTransaction = true
    }

    Database.connect(
        environment.config.tryGetString("ktor.db.url") ?: "",
        driver = "org.postgresql.Driver",
        user = environment.config.tryGetString("ktor.db.user") ?: "",
        password = environment.config.tryGetString("ktor.db.password") ?: "",
        databaseConfig = databaseConfig
    )

    authenticationModule()

    routing {
        openApiEndpoint("/openapi")
        swaggerUiEndpoint("/swagger", "/openapi")
    }

    val storage = DBFileStorage()

    routeControllers(storage)

    val deleteOldTemporaryFileUploadsJob = DeleteOldTemporaryFileUploadsJob(
        1000 * 60 * 60 * 12, // 12 hours
        1000 * 60 * 60 * 24, // 24 hours
        storage
    )

    deleteOldTemporaryFileUploadsJob.dispatch()

    println("Backend started!")
}
