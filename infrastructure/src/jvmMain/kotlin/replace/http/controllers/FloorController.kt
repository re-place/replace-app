package replace.http.controllers

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import org.litote.kmongo.coroutine.CoroutineDatabase
import replace.datastore.MongoFloorRepository
import replace.http.routeRepository
import replace.model.Floor

fun Route.registerFloorRoutes(db: CoroutineDatabase) {
    val floorRepository = MongoFloorRepository(db.getCollection())

    route("/api/floor") {
        routeRepository(floorRepository)

        put("/update") {
            val floor = try {
                call.receive<Floor>()
            } catch (e: Exception) {
                return@put call.respondText(
                    "Invalid request: ${e.message} caused by ${e.cause?.message}",
                    status = HttpStatusCode.BadRequest
                )
            }
            floorRepository.updateOne(floor)
            return@put call.respond(HttpStatusCode.NoContent)
        }
    }
}
