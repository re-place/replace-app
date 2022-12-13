package replace.http.controller

import io.ktor.server.routing.Route
import io.ktor.server.routing.route
import org.litote.kmongo.coroutine.CoroutineDatabase
import replace.datastore.MongoUserRepository
import replace.http.routeRepository

fun Route.registerUserRoutes(db: CoroutineDatabase) {
    val userRepository = MongoUserRepository(db.getCollection())

    route("/api/user") {
        routeRepository(userRepository)
    }
}
