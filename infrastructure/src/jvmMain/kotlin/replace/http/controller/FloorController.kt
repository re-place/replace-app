package replace.http.controller

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import org.bson.types.ObjectId
import org.litote.kmongo.coroutine.CoroutineDatabase
import replace.datastore.MongoBookableEntityRepository
import replace.datastore.MongoFloorRepository
import replace.datastore.MongoRepository
import replace.dto.FloorDto
import replace.dto.toDto
import replace.http.routeRepository
import replace.model.Site
import replace.usecase.floor.CreateFloorUseCase
import replace.usecase.floor.UpdateFloorUseCase

fun Route.registerFloorRoutes(db: CoroutineDatabase) {
    val floorRepository = MongoFloorRepository(db.getCollection())
    val siteRepository = MongoRepository<Site>(db.getCollection())
    val bookableEntityRepository = MongoBookableEntityRepository(db.getCollection())

    route("/api/floor") {
        routeRepository(floorRepository) {
            it.toDto()
        }
        post<FloorDto> {
            executeUseCase {
                CreateFloorUseCase.execute(it, floorRepository, siteRepository)
            }
        }

        get("/{floorId}/bookable-entity") {
            val floorId = call.parameters["floorId"] ?: return@get call.respondText("Missing id", status = HttpStatusCode.BadRequest)

            if (!ObjectId.isValid(floorId)) {
                return@get call.respondText("Id $floorId is not a valid ObjectId", status = HttpStatusCode.BadRequest)
            }

            val bookableEntities = bookableEntityRepository.forFloor(ObjectId(floorId)).map() { it.toDto() }

            call.respond(bookableEntities)
        }

        put<FloorDto> {
            executeUseCase {
                UpdateFloorUseCase.execute(it, floorRepository)
            }
        }
    }
}
