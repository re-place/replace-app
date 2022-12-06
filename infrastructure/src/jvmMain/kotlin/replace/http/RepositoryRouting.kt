package replace.http

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import replace.datastore.Repository
import replace.model.BookableEntity
import replace.model.Booking
import replace.model.ObjectWithId

fun Application.routeAllRepositories(
    bookableEntityRepository: Repository<BookableEntity>,
    bookingRepository: Repository<Booking>,
) {
    routing {
        route("/api/bookable-entity") {
            routeRepository(bookableEntityRepository)
            bookableEntityPost(bookableEntityRepository)
        }
        route("/api/booking") {
            routeRepository(bookingRepository)
            bookingPost(bookingRepository)
        }
    }
}

inline fun <reified T : ObjectWithId> Route.routeRepository(repository: Repository<T>) {
    get {
        try {
            call.respond(repository.getAll().toTypedArray())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    get("{id}") {
        val id = call.parameters["id"] ?: return@get call.respondText("Missing id", status = HttpStatusCode.BadRequest)
        val dbResult = repository.getOne(id)
        if (dbResult == null) {
            call.respondText("No document with id $id", status = HttpStatusCode.NotFound)
        } else {
            call.respond(dbResult)
        }
    }
    post {
        val item = try {
            call.receive<T>()
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
