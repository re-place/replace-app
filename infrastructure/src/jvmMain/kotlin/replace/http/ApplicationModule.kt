package replace.http

import com.typesafe.config.ConfigFactory
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.config.HoconApplicationConfig
import io.ktor.server.config.tryGetString
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import replace.datastore.MongoRepository
import replace.datastore.MongoUserRepository
import replace.model.BookableEntity
import replace.model.Booking
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

    val config = HoconApplicationConfig(ConfigFactory.load())
    val db = getDB(config)

    val bookableEntityRepository = MongoRepository<BookableEntity>(db.getCollection())
    val bookingRepository = MongoRepository<Booking>(db.getCollection())
    val userRepository = MongoUserRepository(db.getCollection())
    sessionModule()
    authenticationModule(userRepository)

    install(SinglePageApplication) {
        folderPath = "static"
        ignoreIfContains = Regex("^/api.*$")
    }

    routeAllRepositories(bookableEntityRepository, bookingRepository, userRepository)
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

    val client = KMongo.createClient("mongodb://$credentials$host:$port").coroutine

    return client.getDatabase(database)
}
