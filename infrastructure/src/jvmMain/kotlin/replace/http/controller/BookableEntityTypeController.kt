package replace.http.controller

import guru.zoroark.tegral.openapi.dsl.schema
import guru.zoroark.tegral.openapi.ktor.describe
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import replace.dto.BookableEntityTypeDto
import replace.dto.CreateBookableEntityTypeDto
import replace.dto.toDto
import replace.http.routeRepository
import replace.model.BookableEntityType
import replace.usecase.bookableentitytype.CreateBookableEntityTypeUseCase

fun Route.registerBookableEntityTypeRoutes() {

    route("/api/bookable-entity-type") {
        routeRepository(BookableEntityType.Companion) {
            it.toDto()
        }
        post<CreateBookableEntityTypeDto> {
            executeUseCase {
                CreateBookableEntityTypeUseCase.execute(it)
            }
        } describe {
            description = "Creates a new bookable entity type"
            body {
                json {
                    schema<CreateBookableEntityTypeDto>()
                }
            }
            200 response {
                description = "The created bookable entity type"
                json {
                    schema<BookableEntityTypeDto>()
                }
            }
        }
    }
}
