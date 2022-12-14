package replace.http

import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.routing
import org.litote.kmongo.coroutine.CoroutineDatabase
import replace.datastore.FileStorage
import replace.http.controller.registerBookableEntityRoutes
import replace.http.controller.registerBookableEntityTypeRoutes
import replace.http.controller.registerBookingRoutes
import replace.http.controller.registerFileRoutes
import replace.http.controller.registerFloorRoutes
import replace.http.controller.registerSiteRoutes
import replace.http.controller.registerTemporaryFileUploadRoutes
import replace.http.controller.registerUserRoutes

fun Application.routeControllers(
    db: CoroutineDatabase,
    fileStorage: FileStorage
) {
    routing {
        authenticate {
            registerBookableEntityRoutes(db)
            registerBookableEntityTypeRoutes(db)
            registerBookingRoutes(db)
            registerFloorRoutes(db, fileStorage)
            registerSiteRoutes(db)
            registerUserRoutes(db)
            registerFileRoutes(db, fileStorage)
            registerTemporaryFileUploadRoutes(db, fileStorage)
        }
    }
}
