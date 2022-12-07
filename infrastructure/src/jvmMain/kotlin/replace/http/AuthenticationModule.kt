package replace.http

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.routing.routing
import io.ktor.server.sessions.Sessions
import io.ktor.server.sessions.cookie
import io.ktor.server.sessions.directorySessionStorage
import io.ktor.server.sessions.maxAge
import replace.datastore.UserRepository
import replace.model.LoginSession
import java.io.File
import kotlin.time.Duration.Companion.hours

fun Application.authenticationModule(
    userRepository: UserRepository,
) {
    install(Sessions) {
        cookie<LoginSession>("LOGIN_SESSION", directorySessionStorage(File(".sessions"), cached = false)) {
            cookie.maxAge = 2.hours
        }
    }
    routing {
        routeAuthentication(userRepository)
    }
}
