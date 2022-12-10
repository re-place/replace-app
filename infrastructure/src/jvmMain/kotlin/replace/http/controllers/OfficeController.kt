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
import org.bson.types.ObjectId
import org.litote.kmongo.coroutine.CoroutineDatabase
import replace.datastore.MongoFloorRepository
import replace.datastore.MongoRepository
import replace.http.routeRepository
import replace.model.Office

fun Route.registerOfficeRoutes(db: CoroutineDatabase) {
    val officeRepository = MongoRepository<Office>(db.getCollection())
    val floorRepository = MongoFloorRepository(db.getCollection())

    route("/api/office") {
        routeRepository(officeRepository)

        put("/update") {
            val office = try {
                call.receive<Office>()
            } catch (e: Exception) {
                return@put call.respondText(
                    "Invalid request: ${e.message} caused by ${e.cause?.message}",
                    status = HttpStatusCode.BadRequest
                )
            }

            officeRepository.updateOne(office)
        }

        get("/{officeId}/floor") {
            val officeId = call.parameters["officeId"] ?: return@get call.respondText("Missing id", status = HttpStatusCode.BadRequest)

            if (!ObjectId.isValid(officeId)) {
                return@get call.respondText("Id $officeId is not a valid ObjectId", status = HttpStatusCode.BadRequest)
            }

            val floors = floorRepository.forOffice(ObjectId(officeId))

            call.respond(floors)
        }
    }
}
