package replace.http.controller

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import org.bson.types.ObjectId
import org.litote.kmongo.coroutine.CoroutineDatabase
import replace.datastore.MongoRepository
import replace.http.routeRepository
import replace.model.Location

fun Route.registerOfficeRoutes(db: CoroutineDatabase) {
    val locationRepository = MongoRepository<Location>(db.getCollection())

    route("/api/office") {
        routeRepository(locationRepository)

        get("/{officeId}/floor") {
            val officeId = call.parameters["officeId"] ?: return@get call.respondText("Missing id", status = HttpStatusCode.BadRequest)

            if (!ObjectId.isValid(officeId)) {
                return@get call.respondText("Id $officeId is not a valid ObjectId", status = HttpStatusCode.BadRequest)
            }

//            val floors = floorRepository.forOffice(ObjectId(officeId))

//            call.respond(floors)
        }
    }
}
