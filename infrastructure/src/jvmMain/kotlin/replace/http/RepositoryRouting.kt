package replace.http

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.bson.types.ObjectId
import replace.datastore.Repository
import replace.model.BookableEntity
import replace.model.Booking
import replace.model.ObjectWithId

fun Application.routeAllRepositories(
    bookableEntityRepository: Repository<BookableEntity>,
    bookingRepository: Repository<Booking>,
) {
    routing {
        get("/foo") {
            call.respond(Booking(listOf()).also { it._id = ObjectId() })
        }
        get("/foos") {
            call.respond(listOf(Booking(listOf()).also { it._id = ObjectId() }))
        }
        route("/api/bookable-entity") {
            routeRepository(bookableEntityRepository)
        }
        route("/api/booking") {
            routeRepository(bookingRepository)
        }
    }
}

inline fun <reified T : ObjectWithId> Route.routeRepository(repository: Repository<T>) {
    get {
        try {
            // .toTypedArray() is necessary because of a bug with reified inline functions and coroutines
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
