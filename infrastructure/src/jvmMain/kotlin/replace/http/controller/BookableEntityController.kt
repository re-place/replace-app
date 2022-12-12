package replace.http.controller

import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import org.litote.kmongo.coroutine.CoroutineDatabase
import replace.datastore.MongoRepository
import replace.dto.BookableEntityDto
import replace.http.routeRepository
import replace.model.BookableEntity
import replace.model.BookableEntityType
import replace.usecase.bookableentity.CreateBookableEntityUseCase
import replace.usecase.bookableentity.UpdateBookableEntityUseCase

fun Route.registerBookableEntityRoutes(db: CoroutineDatabase) {
    val bookableEntityRepository = MongoRepository<BookableEntity>(db.getCollection())
    val bookableEntityTypeRepository = MongoRepository<BookableEntityType>(db.getCollection())

    route("/api/bookable-entity") {
        routeRepository(bookableEntityRepository)
        post<BookableEntityDto> {
            executeUseCase {
                CreateBookableEntityUseCase.execute(it, bookableEntityRepository, bookableEntityTypeRepository)
            }
        }
        put<BookableEntityDto> {
            executeUseCase {
                UpdateBookableEntityUseCase.execute(it, bookableEntityRepository, bookableEntityTypeRepository)
            }
        }
    }
}
