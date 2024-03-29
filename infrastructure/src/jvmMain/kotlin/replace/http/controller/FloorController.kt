package replace.http.controller

import guru.zoroark.tegral.openapi.dsl.schema
import guru.zoroark.tegral.openapi.ktor.describe
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import replace.datastore.FileStorage
import replace.dto.BookableEntityDto
import replace.dto.CreateFloorDto
import replace.dto.FloorDto
import replace.dto.UpdateFloorDto
import replace.dto.toDto
import replace.http.routeRepository
import replace.model.BookableEntities
import replace.model.BookableEntity
import replace.model.Floor
import replace.usecase.floor.CreateFloorUseCase
import replace.usecase.floor.DeleteFloorUseCase
import replace.usecase.floor.UpdateFloorUseCase

fun Route.registerFloorRoutes(fileStorage: FileStorage) {
    route("/api/floor") {
        delete("/{floorId}") {
            val floorId = call.parameters["floorId"]
            executeUseCase {
                if (floorId != null) {
                    DeleteFloorUseCase.execute(floorId, fileStorage)
                }

                call.respond(HttpStatusCode.NoContent)
            }
        }

        routeRepository(Floor.Companion) {
            it.toDto()
        }

        post<CreateFloorDto> {
            executeUseCase {
                CreateFloorUseCase.execute(
                    it,
                    fileStorage,
                )
            }
        } describe {
            description = "Creates a new floor"
            body {
                json {
                    schema<CreateFloorDto>()
                }
            }
            200 response {
                description = "The created floor"
                json {
                    schema<FloorDto>()
                }
            }
        }

        get("/{floorId}/bookable-entity") {
            val floorId = call.parameters["floorId"] ?: return@get call.respondText("Missing id", status = HttpStatusCode.BadRequest)

            val bookableEntityDtos = newSuspendedTransaction {
                BookableEntity
                    .find(BookableEntities.floor_id eq floorId)
                    .orderBy(BookableEntities.index to SortOrder.ASC)
                    .with(BookableEntity::children)
                    .map { it.toDto(listOf(BookableEntity::children)) }
            }

            call.respond(bookableEntityDtos)
        } describe {
            "floorId" pathParameter {
                description = "The id of the floor"
                schema("<id>")
            }
            description = "Gets all bookable entities for a floor"
            200 response {
                description = "The bookable entities for the floor"
                json {
                    schema<List<BookableEntityDto>>()
                }
            }
        }

        put<UpdateFloorDto> {
            executeUseCase {
                UpdateFloorUseCase.execute(it, fileStorage)
            }
        } describe {
            description = "Updates a floor"
            body {
                json {
                    schema<UpdateFloorDto>()
                }
            }
            200 response {
                description = "The updated floor"
                json {
                    schema<FloorDto>()
                }
            }
        }
    }
}
