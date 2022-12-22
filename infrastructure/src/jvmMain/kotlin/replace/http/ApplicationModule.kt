package replace.http

import com.typesafe.config.ConfigFactory
import guru.zoroark.tegral.openapi.ktor.TegralOpenApiKtor
import guru.zoroark.tegral.openapi.ktor.openApiEndpoint
import guru.zoroark.tegral.openapi.ktorui.TegralSwaggerUiKtor
import guru.zoroark.tegral.openapi.ktorui.swaggerUiEndpoint
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.config.HoconApplicationConfig
import io.ktor.server.config.tryGetString
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.resources.Resources
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import replace.datastore.LocalStorage
import replace.datastore.MongoUserRepository
import replace.plugin.SinglePageApplication
import replace.serializer.ObjectIdSerializer

fun Application.applicationModule() {
    install(CORS) {
        anyHost() // TODO: Don't do this in production
        allowHeader("SESSION_TOKEN")
        allowHeader(HttpHeaders.ContentType)
        exposeHeader("SESSION_TOKEN")
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

    val config = HoconApplicationConfig(ConfigFactory.load())
    val db = getDB(config)

    val userRepository = MongoUserRepository(db.getCollection())

    sessionModule()
    authenticationModule(userRepository)

    install(SinglePageApplication) {
        folderPath = "static"
        ignoreIfContains = Regex("^/api.*$")
    }

    routing {
        openApiEndpoint("/openapi")
        swaggerUiEndpoint("/swagger", "/openapi")
    }

    val storage = LocalStorage()

    routeControllers(db, storage)
}

fun getDB(config: HoconApplicationConfig): CoroutineDatabase {
    val host = config.tryGetString("database.host") ?: "localhost"
    val port = config.tryGetString("database.port") ?: "27017"
    val user = config.tryGetString("database.user") ?: ""
    val password = config.tryGetString("database.password")?.prependIndent(":") ?: ""
    val database = config.tryGetString("database.database") ?: "replace-app"

    var credentials = "$user$password"

    if (credentials.isNotBlank()) {
        credentials += "@"
    }

    val mongoUri = config.tryGetString("database.uri")
        ?: "mongodb://$credentials$host:$port"

    val client = KMongo.createClient(mongoUri).coroutine

    return client.getDatabase(database)
}
