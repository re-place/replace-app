package replace.http.controllers

import io.ktor.server.routing.Route
import io.ktor.server.routing.route
import org.litote.kmongo.coroutine.CoroutineDatabase
import replace.datastore.MongoFloorRepository
import replace.http.routeRepository

fun Route.registerFloorRoutes(db: CoroutineDatabase) {
    val floorRepository = MongoFloorRepository(db.getCollection())

    route("/api/floor") {
        routeRepository(floorRepository)
    }
}
