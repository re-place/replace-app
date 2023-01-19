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
import org.bson.types.ObjectId
import org.litote.kmongo.coroutine.CoroutineDatabase
import replace.datastore.FileStorage
import replace.datastore.MongoTemporaryFileRepository
import replace.dto.TemporaryFileUploadDto
import replace.usecase.temporaryfileupload.CreateTemporaryFileUploadUseCase
import replace.usecase.temporaryfileupload.DeleteTemporaryFileUploadUseCase
import java.util.UUID

fun Route.registerTemporaryFileUploadRoutes(db: CoroutineDatabase, fileStorage: FileStorage) {
    val temporaryFileUploadRepository = MongoTemporaryFileRepository(db.getCollection())

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
                        temporaryFileUploadRepository,
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
            if (!ObjectId.isValid(route.id)) {
                return@get call.respondText("Id ${route.id} is not a valid ObjectId", status = HttpStatusCode.BadRequest)
            }

            val dbResult = temporaryFileUploadRepository.findOneById(ObjectId(route.id))
                ?: return@get call.respondText("Temporary file upload with id ${route.id} not found", status = HttpStatusCode.NotFound)

            if (!fileStorage.exists(dbResult.path)) {
                return@get call.respondText("Temporary file upload with id ${route.id} not found", status = HttpStatusCode.NotFound)
            }

            call.response.header(
                HttpHeaders.ContentDisposition,
                ContentDisposition.Inline.withParameter(
                    ContentDisposition.Parameters.FileName, "${dbResult.name}.${dbResult.extension}"
                ).toString()
            )

            val mime = dbResult.mime ?: ContentType.Application.OctetStream.toString()

            call.respondBytes(ContentType.parse(mime)) { fileStorage.readFile(dbResult.path).readBytes() }
        } describe {
            description = "Gets a temporary file upload by id"
            "id" pathParameter {
                description = "The id of the temporary file"
                schema(ObjectId().toString())
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
                DeleteTemporaryFileUploadUseCase.execute(route.id, temporaryFileUploadRepository, fileStorage)
                return@delete call.respond(HttpStatusCode.NoContent)
            }
        } describe {
            "id" pathParameter {
                description = "The id of the temporary file"
                schema(ObjectId().toString())
            }
            description = "Deletes a temporary file upload by id"
            204 response {
                description = "The temporary file upload was deleted"
            }
        }
    }
}
