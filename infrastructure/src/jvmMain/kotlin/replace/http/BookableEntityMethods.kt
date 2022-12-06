package replace.http

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import replace.datastore.Repository
import replace.model.BookableEntity

fun Route.bookableEntityPost(repository: Repository<BookableEntity>) {
    post {
        val item = try {
            call.receive<BookableEntity>()
        } catch (e: Exception) {
            return@post call.respondText(
                "Invalid request: ${e.message} caused by ${e.cause?.message}",
                status = HttpStatusCode.BadRequest
            )
        }
        val dbResult = repository.insertOne(item)
        if (dbResult == null) {
            call.respondText("Failed to insert document", status = HttpStatusCode.InternalServerError)
        } else {
            call.respond(dbResult)
        }
    }
}

