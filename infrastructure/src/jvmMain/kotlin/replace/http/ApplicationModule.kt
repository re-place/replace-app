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
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import org.jetbrains.exposed.sql.Database
import replace.datastore.LocalFileStorage
import replace.job.DeleteOldTemporaryFileUploadsJob
import replace.plugin.SinglePageApplication
import replace.serializer.ObjectIdSerializer

fun Application.applicationModule() {
    install(CORS) {
        anyHost() // TODO: Don't do this in production
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
                serializersModule = SerializersModule {
                    contextual(ObjectIdSerializer)
                }
            }
        )
    }
    // install type-safe routing
    install(Resources)

    install(TegralOpenApiKtor) {
        title = "Replace API"
        description = "API for the Replace application"
        version = "2022.1-SNAPSHOT"
    }

    install(TegralSwaggerUiKtor)

    Database.connect(
        environment.config.tryGetString("ktor.db.url") ?: "",
        driver = "org.postgresql.Driver",
        user = environment.config.tryGetString("ktor.database.user") ?: "",
        password = environment.config.tryGetString("ktor.database.password") ?: ""
    )

    sessionModule()
    authenticationModule()

    install(SinglePageApplication) {
        folderPath = "static"
        ignoreIfContains = Regex("^/api.*$")
    }

    routing {
        openApiEndpoint("/openapi")
        swaggerUiEndpoint("/swagger", "/openapi")
    }

    val storage = LocalFileStorage()

    routeControllers(storage)

    val deleteOldTemporaryFileUploadsJob = DeleteOldTemporaryFileUploadsJob(
        1000 * 60 * 60 * 12, // 12 hours
        1000 * 60 * 60 * 24, // 24 hours
        storage
    )

    deleteOldTemporaryFileUploadsJob.dispatch()
}
