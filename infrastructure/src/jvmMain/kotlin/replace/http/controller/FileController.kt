package replace.http.controller

import guru.zoroark.tegral.openapi.ktor.describe
import io.ktor.http.ContentDisposition
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.resources.get
import io.ktor.server.response.header
import io.ktor.server.response.respondBytes
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import org.bson.types.ObjectId
import org.litote.kmongo.coroutine.CoroutineDatabase
import replace.datastore.MongoRepository
import replace.datastore.Storage
import replace.model.File

fun Route.registerFileRoutes(db: CoroutineDatabase, storage: Storage) {
    val fileRepository = MongoRepository<File>(db.getCollection())

    route("/api/file") {
        get<Routing.ById> { route ->
            if (!ObjectId.isValid(route.id)) {
                return@get call.respondText("Id ${route.id} is not a valid ObjectId", status = HttpStatusCode.BadRequest)
            }

            val dbResult = fileRepository.findOneById(ObjectId(route.id))
                ?: return@get call.respondText("Temporary file upload with id ${route.id} not found", status = HttpStatusCode.NotFound)

            val file = storage.getFile(dbResult.path)

            if (!file.exists()) {
                return@get call.respondText("Temporary file upload with id ${route.id} not found", status = HttpStatusCode.NotFound)
            }

            call.response.header(
                HttpHeaders.ContentDisposition,
                ContentDisposition.Inline.withParameter(
                    ContentDisposition.Parameters.FileName, "${dbResult.name}.${dbResult.extension}"
                ).toString()
            )

            val mime = dbResult.mime ?: ContentType.Application.OctetStream.toString()

            call.respondBytes(ContentType.parse(mime)) { file.readBytes() }
        } describe {
            description = "Gets a temporary file upload by id"
            200 response {
                description = "The temporary file upload"
            }
        }
    }
}
