package replace.http

import com.typesafe.config.ConfigFactory
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import replace.datastore.MongoRepository
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

    routeAllRepositories(
        bookableEntityRepository = MongoRepository(db.getCollection()),
        bookingRepository = MongoRepository(db.getCollection()),
    )
}

fun getDB(config: HoconApplicationConfig): CoroutineDatabase {
    val host = config.tryGetString("ktor.database.host") ?: "localhost"
    val port = config.tryGetString("ktor.database.port") ?: "27017"
    val user = config.tryGetString("ktor.database.user") ?: "admin"
    val password = config.tryGetString("ktor.database.password") ?: "password"
    val database = config.tryGetString("ktor.database.database") ?: "replace-app"

    val client = KMongo.createClient("mongodb://$user:$password@$host:$port").coroutine
    return client.getDatabase(database)
}
