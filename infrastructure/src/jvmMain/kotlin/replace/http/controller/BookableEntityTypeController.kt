package replace.http.controller

import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.litote.kmongo.coroutine.CoroutineDatabase
import replace.datastore.MongoBookableEntityTypeRepository
import replace.dto.BookableEntityTypeDto
import replace.http.routeRepository
import replace.usecase.bookableentitytype.CreateBookableEntityTypeUseCase

fun Route.registerBookableEntityTypeRoutes(db: CoroutineDatabase) {
    val bookableEntityTypeRepository = MongoBookableEntityTypeRepository(db.getCollection())

    route("/api/bookable-entity-type") {
        routeRepository(bookableEntityTypeRepository)
        post<BookableEntityTypeDto> {
            executeUseCase {
                CreateBookableEntityTypeUseCase.execute(it, bookableEntityTypeRepository)
            }
        }
    }
}
