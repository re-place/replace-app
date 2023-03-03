package replace.http.controller

import guru.zoroark.tegral.openapi.dsl.schema
import guru.zoroark.tegral.openapi.ktor.describe
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import replace.dto.BookableEntityDto
import replace.dto.CreateBookableEntityDto
import replace.dto.UpdateBookableEntityDto
import replace.dto.UpdateBookableEntityOrderDto
import replace.dto.toDto
import replace.http.routeRepository
import replace.model.BookableEntity
import replace.usecase.bookableentity.CreateBookableEntityUseCase
import replace.usecase.bookableentity.UpdateBookableEntityOrderUseCase
import replace.usecase.bookableentity.UpdateBookableEntityUseCase

fun Route.registerBookableEntityRoutes() {

    route("/api/bookable-entity") {
        routeRepository(BookableEntity.Companion) {
            it.toDto()
        }

        post<CreateBookableEntityDto> {
            executeUseCase {
                CreateBookableEntityUseCase.execute(it)
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
                UpdateBookableEntityUseCase.execute(it)
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

        put<UpdateBookableEntityOrderDto>("/order") {
            executeUseCase {
                UpdateBookableEntityOrderUseCase.execute(it)
            }
        } describe {
            description = "Updates the order of bookable entities"
            body {
                json {
                    schema<UpdateBookableEntityOrderDto>()
                }
            }
            204 response {
                description = "The order of bookable entities has been updated"
            }
        }
    }
}
