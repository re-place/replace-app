package replace.http.controller

import io.ktor.server.routing.Route
import io.ktor.server.routing.route
import replace.dto.toDto
import replace.http.routeRepository
import replace.model.User

fun Route.registerUserRoutes() {
    route("/api/user") {
        routeRepository(User.Companion) {
            it.toDto()
        }
    }
}
