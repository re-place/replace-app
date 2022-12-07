package replace.http

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import replace.datastore.UserRepository
import replace.model.UserSession

fun Application.authenticationModule(
    userRepository: UserRepository,
) {
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
        routeAuthentication(userRepository)
    }
}
