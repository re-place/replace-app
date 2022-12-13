package replace.http.controller

import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.litote.kmongo.coroutine.CoroutineDatabase
import replace.datastore.MongoFloorRepository
import replace.datastore.MongoRepository
import replace.dto.FloorDto
import replace.http.routeRepository
import replace.model.Site
import replace.usecase.floor.CreateFloorUseCase

fun Route.registerFloorRoutes(db: CoroutineDatabase) {
    val floorRepository = MongoFloorRepository(db.getCollection())
    val siteRepository = MongoRepository<Site>(db.getCollection())

    route("/api/floor") {
        routeRepository(floorRepository)
        post<FloorDto> {
            executeUseCase {
                CreateFloorUseCase.execute(it, floorRepository, siteRepository)
            }
        }
    }
}
