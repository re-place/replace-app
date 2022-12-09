package replace.http

import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.routing
import org.litote.kmongo.coroutine.CoroutineDatabase
import replace.http.controller.registerBookableEntityRoutes
import replace.http.controller.registerBookingRoutes
import replace.http.controller.registerFloorRoutes
import replace.http.controller.registerOfficeRoutes
import replace.http.controller.registerUserRoutes

fun Application.routeControllers(
    db: CoroutineDatabase
) {
    routing {
        authenticate {
            this.registerBookableEntityRoutes(db)
            this.registerBookingRoutes(db)
            this.registerFloorRoutes(db)
            this.registerOfficeRoutes(db)
            this.registerUserRoutes(db)
        }
    }
}
