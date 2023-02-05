package replace.http

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.auth.authentication
import io.ktor.server.auth.session
import io.ktor.server.response.respondText
import io.ktor.server.routing.routing
import replace.model.UserSession

fun Application.authenticationModule() {
    authentication {
        session<UserSession> {
            validate { session ->
                if (session.userId !== null) {
                    session
                } else {
                    null
                }
            }

            challenge {
                call.respondText("Not authenticated", status = HttpStatusCode.Unauthorized)
            }
        }
    }

    routing {
        routeAuthentication()
    }
}
