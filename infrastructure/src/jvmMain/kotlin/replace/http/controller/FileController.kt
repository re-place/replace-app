package replace.http.controller

import guru.zoroark.tegral.openapi.dsl.schema
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
import io.ktor.server.routing.route
import io.swagger.v3.oas.models.media.FileSchema
import org.bson.types.ObjectId
import replace.datastore.FileStorage
import replace.model.File

fun Route.registerFileRoutes(fileStorage: FileStorage) {
    route("/api/file") {
        get<Routing.ById> { route ->
            val file = File.findById(route.id)

            if (file === null) {
                call.respondText("No File with id ${route.id} found", status = HttpStatusCode.NotFound)
                return@get
            }

            if (!fileStorage.exists(file.path)) {
                call.respondText("File with id ${route.id} not found", status = HttpStatusCode.NotFound)
                return@get
            }

            call.response.header(
                HttpHeaders.ContentDisposition,
                ContentDisposition.Inline.withParameter(
                    ContentDisposition.Parameters.FileName, "${file.name}.${file.extension}"
                ).toString()
            )

            val mime = file.mime ?: ContentType.Application.OctetStream.toString()

            call.respondBytes(ContentType.parse(mime)) { fileStorage.readFile(file.path).readBytes() }
        } describe {
            description = "Gets a temporary file upload by id"
            "id" pathParameter {
                description = "The id of the file"
                schema(ObjectId().toString())
            }
            200 response {
                description = "The temporary file upload"
                "*" content {
                    schema = FileSchema()
                }
            }
        }
    }
}
