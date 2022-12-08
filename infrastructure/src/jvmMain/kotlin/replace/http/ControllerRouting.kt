package replace.http

import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.routing
import org.litote.kmongo.coroutine.CoroutineDatabase
import replace.http.controllers.registerBookableEntityRoutes
import replace.http.controllers.registerBookingRoutes
import replace.http.controllers.registerFloorRoutes
import replace.http.controllers.registerOfficeRoutes
import replace.http.controllers.registerUserRoutes

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
