package replace.http

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
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
            routeRepositoryBase(bookableEntityRepository)
            routeBooking(bookableEntityRepository)
        }
        route("/api/booking") {
            routeRepositoryBase(bookingRepository)
            routeBooking(bookingRepository)
        }
    }
}

inline fun <reified T : ObjectWithId> Route.routeRepositoryBase(repository: Repository<T>) {
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
}
