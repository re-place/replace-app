package replace.http.controller

import guru.zoroark.tegral.openapi.dsl.schema
import guru.zoroark.tegral.openapi.ktor.describe
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.litote.kmongo.coroutine.CoroutineDatabase
import replace.datastore.MongoBookableEntityTypeRepository
import replace.dto.BookableEntityTypeDto
import replace.dto.toDto
import replace.http.routeRepository
import replace.usecase.bookableentitytype.CreateBookableEntityTypeUseCase

fun Route.registerBookableEntityTypeRoutes(db: CoroutineDatabase) {
    val bookableEntityTypeRepository = MongoBookableEntityTypeRepository(db.getCollection())

    route("/api/bookable-entity-type") {
        routeRepository(bookableEntityTypeRepository) {
            it.toDto()
        }
        post<BookableEntityTypeDto> {
            executeUseCase {
                CreateBookableEntityTypeUseCase.execute(it, bookableEntityTypeRepository)
            }
        } describe {
            description = "Creates a new bookable entity type"
            body {
                json {
                    schema<BookableEntityTypeDto>()
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
