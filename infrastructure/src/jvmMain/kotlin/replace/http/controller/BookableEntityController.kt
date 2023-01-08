package replace.http.controller

import guru.zoroark.tegral.openapi.dsl.schema
import guru.zoroark.tegral.openapi.ktor.describe
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import org.litote.kmongo.coroutine.CoroutineDatabase
import replace.datastore.MongoRepository
import replace.dto.BookableEntityDto
import replace.dto.toDto
import replace.http.routeRepository
import replace.model.BookableEntity
import replace.model.BookableEntityType
import replace.usecase.bookableentity.CreateBookableEntityUseCase
import replace.usecase.bookableentity.UpdateBookableEntityUseCase

fun Route.registerBookableEntityRoutes(db: CoroutineDatabase) {
    val bookableEntityRepository = MongoRepository<BookableEntity>(db.getCollection())
    val bookableEntityTypeRepository = MongoRepository<BookableEntityType>(db.getCollection())

    route("/api/bookable-entity") {
        routeRepository(bookableEntityRepository) {
            it.toDto()
        }

        post<BookableEntityDto> {
            executeUseCase {
                CreateBookableEntityUseCase.execute(it, bookableEntityRepository, bookableEntityTypeRepository)
            }
        } describe {
            description = "Creates a new bookable entity"
            body {
                json {
                    schema<BookableEntityDto>()
                }
            }
            200 response {
                description = "The created bookable entity"
                json {
                    schema<BookableEntityDto>()
                }
            }
        }

        put<BookableEntityDto> {
            executeUseCase {
                UpdateBookableEntityUseCase.execute(it, bookableEntityRepository)
            }
        } describe {
            description = "Updates a bookable entity"
            body {
                json {
                    schema<BookableEntityDto>()
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
