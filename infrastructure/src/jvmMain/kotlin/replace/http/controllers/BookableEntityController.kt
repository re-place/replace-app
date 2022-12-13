package replace.http.controllers

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import org.litote.kmongo.coroutine.CoroutineDatabase
import replace.datastore.MongoRepository
import replace.http.routeRepository
import replace.model.BookableEntity
import replace.model.Floor

fun Route.registerBookableEntityRoutes(db: CoroutineDatabase) {
    val repository = MongoRepository<BookableEntity>(db.getCollection())

    route("/api/bookable-entity") {
        routeRepository(repository)

        put("/update") {
            val bookableEntity = try {
                call.receive<BookableEntity>()
            } catch (e: Exception) {
                return@put call.respondText(
                    "Invalid request: ${e.message} caused by ${e.cause?.message}",
                    status = HttpStatusCode.BadRequest
                )
            }
            repository.updateOne(bookableEntity)
            return@put call.respond(HttpStatusCode.NoContent)
        }
    }
}
