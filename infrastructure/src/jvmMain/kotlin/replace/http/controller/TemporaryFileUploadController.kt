package replace.http.controller

import guru.zoroark.tegral.openapi.dsl.schema
import guru.zoroark.tegral.openapi.ktor.describe
import io.ktor.http.ContentDisposition
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.server.application.call
import io.ktor.server.request.receiveMultipart
import io.ktor.server.resources.delete
import io.ktor.server.resources.get
import io.ktor.server.response.header
import io.ktor.server.response.respond
import io.ktor.server.response.respondBytes
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.swagger.v3.oas.models.media.FileSchema
import io.swagger.v3.oas.models.media.MapSchema
import org.jetbrains.exposed.sql.transactions.transaction
import replace.datastore.FileStorage
import replace.dto.TemporaryFileUploadDto
import replace.model.TemporaryFile
import replace.usecase.temporaryfileupload.CreateTemporaryFileUploadUseCase
import replace.usecase.temporaryfileupload.DeleteTemporaryFileUploadUseCase
import java.util.UUID

fun Route.registerTemporaryFileUploadRoutes(fileStorage: FileStorage) {
    route("/api/temporary-file-upload") {
        post {
            executeUseCase {
                val multipart = call.receiveMultipart()
                val temporaryFileUploadDtos = mutableListOf<TemporaryFileUploadDto>()

                multipart.forEachPart {

                    if (it !is PartData.FileItem) {
                        it.dispose()
                        return@forEachPart
                    }

                    val name = it.originalFileName ?: UUID.randomUUID().toString()
                    val newFile = CreateTemporaryFileUploadUseCase.execute(
                        name,
                        it.streamProvider(),
                        fileStorage
                    )
                    temporaryFileUploadDtos.add(newFile)
                }

                return@post call.respond(temporaryFileUploadDtos)
            }
        } describe {
            description = "Creates a new temporary file uploads"
            body {
                required = true
                "multipart/form-data" content {
                    schema = MapSchema()
                        .addPatternProperty("*", FileSchema().description("The File to upload"))
                }
            }
            200 response {
                description = "The created temporary file uploads"
                json {
                    schema<List<TemporaryFileUploadDto>>()
                }
            }
        }

        get<Routing.ById> { route ->

            val file = transaction { TemporaryFile.findById(route.id) }

            if (file === null) {
                call.respondText("No File with id ${route.id} found", status = HttpStatusCode.NotFound)
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
                description = "The id of the temporary file"
                schema(String)
            }
            200 response {
                description = "The temporary file upload"
                "*" content {
                    schema = FileSchema()
                }
            }
        }

        delete<Routing.ById> { route ->
            executeUseCase {
                DeleteTemporaryFileUploadUseCase.execute(route.id, fileStorage)
                return@delete call.respond(HttpStatusCode.NoContent)
            }
        } describe {
            "id" pathParameter {
                description = "The id of the temporary file"
                schema(String)
            }
            description = "Deletes a temporary file upload by id"
            204 response {
                description = "The temporary file upload was deleted"
            }
        }
    }
}
