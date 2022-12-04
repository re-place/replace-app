package replace

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.Json.Default.serializersModule
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
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
    val client = KMongo.createClient().coroutine
    val db = client.getDatabase("replace")
    configureRouting(
        bookingRepository = MongoRepository(db.getCollection()),
    )
}
