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
import replace.model.OfficeBuilding

fun Route.registerOfficeBuildingRoutes(db: CoroutineDatabase) {
    val officeBuildingRepository = MongoRepository<OfficeBuilding>(db.getCollection())
    val floorRepository = MongoFloorRepository(db.getCollection())

    route("/api/office-building") {
        routeRepository(officeBuildingRepository)

        put("/update") {
            val office = try {
                call.receive<OfficeBuilding>()
            } catch (e: Exception) {
                return@put call.respondText(
                    "Invalid request: ${e.message} caused by ${e.cause?.message}",
                    status = HttpStatusCode.BadRequest
                )
            }

            officeBuildingRepository.updateOne(office)
            return@put call.respond(HttpStatusCode.NoContent)
        }

        get("/{officeBuildingId}/floor") {
            val officeBuildingId = call.parameters["officeBuildingId"] ?: return@get call.respondText("Missing id", status = HttpStatusCode.BadRequest)

            if (!ObjectId.isValid(officeBuildingId)) {
                return@get call.respondText("Id $officeBuildingId is not a valid ObjectId", status = HttpStatusCode.BadRequest)
            }

            val floors = floorRepository.forOfficeBuilding(ObjectId(officeBuildingId))

            call.respond(floors)
        }
    }
}
