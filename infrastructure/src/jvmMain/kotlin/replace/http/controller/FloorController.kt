package replace.http.controller

import guru.zoroark.tegral.openapi.dsl.schema
import guru.zoroark.tegral.openapi.ktor.describe
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
        } describe {
            description = "Creates a new floor"
            200 response {
                description = "The created floor"
                json {
                    schema<FloorDto>()
                }
            }
        }

        get("/{floorId}/bookable-entity") {
            val floorId = call.parameters["floorId"] ?: return@get call.respondText("Missing id", status = HttpStatusCode.BadRequest)

            if (!ObjectId.isValid(floorId)) {
                return@get call.respondText("Id $floorId is not a valid ObjectId", status = HttpStatusCode.BadRequest)
            }

            val bookableEntities = bookableEntityRepository.forFloor(ObjectId(floorId)).map() { it.toDto() }

            call.respond(bookableEntities)
        } describe {
            "floorId" pathParameter {
                description = "The id of the floor"
                schema(ObjectId().toString())
            }
            description = "Gets all bookable entities for a floor"
            200 response {
                description = "The bookable entities for the floor"
                json {
                    schema<List<FloorDto>>()
                }
            }
        }

        put<FloorDto> {
            executeUseCase {
                UpdateFloorUseCase.execute(it, floorRepository)
            }
        } describe {
            description = "Updates a floor"
            200 response {
                description = "The updated floor"
                json {
                    schema<FloorDto>()
                }
            }
        }
    }
}
