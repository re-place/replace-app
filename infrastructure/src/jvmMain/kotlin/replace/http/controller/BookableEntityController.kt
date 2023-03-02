package replace.http.controller

import guru.zoroark.tegral.openapi.dsl.schema
import guru.zoroark.tegral.openapi.ktor.describe
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import replace.dto.BookableEntityDto
import replace.dto.CreateBookableEntityDto
import replace.dto.UpdateBookableEntityDto
import replace.dto.toDto
import replace.http.routeRepository
import replace.model.BookableEntity
import replace.usecase.bookableentity.CreateBookableEntityUseCase
import replace.usecase.bookableentity.UpdateBookableEntityUseCase

fun Route.registerBookableEntityRoutes() {

    route("/api/bookable-entity") {
        routeRepository(BookableEntity.Companion) {
            it.toDto()
        }

        post<CreateBookableEntityDto> {
            executeUseCase {
                call.respond(CreateBookableEntityUseCase.execute(it))
            }
        } describe {
            description = "Creates a new bookable entity"
            body {
                json {
                    schema<CreateBookableEntityDto>()
                }
            }
            200 response {
                description = "The created bookable entity"
                json {
                    schema<BookableEntityDto>()
                }
            }
        }

        put<UpdateBookableEntityDto> {
            executeUseCase {
                call.respond(UpdateBookableEntityUseCase.execute(it))
            }
        } describe {
            description = "Updates a bookable entity"
            body {
                json {
                    schema<UpdateBookableEntityDto>()
                }
            }
            200 response {
                description = "The updated bookable entity"
                json {
                    schema<BookableEntityDto>()
                }
            }
        }
    }
}
