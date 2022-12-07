package replace.http

import com.typesafe.config.ConfigFactory
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
    install(SinglePageApplication) {
        folderPath = "static"
        ignoreIfContains = Regex("^/api.*$")
    }

    val config = HoconApplicationConfig(ConfigFactory.load())
    val db = getDB(config)

    val bookableEntityRepository = MongoRepository<BookableEntity>(db.getCollection())
    val bookingRepository = MongoRepository<Booking>(db.getCollection())
    val userRepository = MongoUserRepository(db.getCollection())
    routeAllRepositories(bookableEntityRepository, bookingRepository, userRepository)
    authenticationModule(userRepository)
}

fun getDB(config: HoconApplicationConfig): CoroutineDatabase {
    val host = config.tryGetString("ktor.database.host") ?: "localhost"
    val port = config.tryGetString("ktor.database.port") ?: "27017"
    val user = config.tryGetString("ktor.database.user") ?: "admin"
    val password = config.tryGetString("ktor.database.password") ?: "password"
    val database = config.tryGetString("ktor.database.database") ?: "replace-app"

    // TODO: Should we just use a blank password by default for development
//    val client = KMongo.createClient("mongodb://$user:$password@$host:$port").coroutine
    val client = KMongo.createClient().coroutine
    return client.getDatabase(database)
}
