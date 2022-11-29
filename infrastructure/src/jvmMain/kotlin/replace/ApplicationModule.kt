package replace

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.applicationModule() {
    routing {
        get("/api/hello") {
            call.respondText("Hello World!")
        }
    }
}
