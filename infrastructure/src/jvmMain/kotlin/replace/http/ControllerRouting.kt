package replace.http

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import replace.datastore.FileStorage
import replace.http.controller.*

fun Application.routeControllers(
    fileStorage: FileStorage
) {
    routing {
        authenticate("internal-session") {
            registerBookableEntityRoutes()
            registerBookableEntityTypeRoutes()
            registerBookingRoutes()
            registerFloorRoutes(fileStorage)
            registerSiteRoutes(fileStorage)
            registerUserRoutes()
            registerFileRoutes(fileStorage)
            registerTemporaryFileUploadRoutes(fileStorage)
        }
    }
}
