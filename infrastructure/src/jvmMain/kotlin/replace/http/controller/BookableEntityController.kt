package replace.http.controller

import io.ktor.server.routing.Route
import io.ktor.server.routing.route
import org.litote.kmongo.coroutine.CoroutineDatabase
import replace.datastore.MongoRepository
import replace.http.routeRepository
import replace.model.BookableEntity

fun Route.registerBookableEntityRoutes(db: CoroutineDatabase) {
    val repository = MongoRepository<BookableEntity>(db.getCollection())

    route("/api/bookable-entity") {
        routeRepository(repository)
    }
}
