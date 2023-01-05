package replace.http

import io.ktor.server.application.Application
import io.ktor.server.application.createApplicationPlugin
import io.ktor.server.application.install
import io.ktor.server.sessions.Sessions
import io.ktor.server.sessions.cookie
import io.ktor.server.sessions.directorySessionStorage
import io.ktor.server.sessions.get
import io.ktor.server.sessions.sessions
import io.ktor.server.sessions.set
import replace.model.UserSession
import java.io.File

fun Application.sessionModule() {
    install(Sessions) {
        cookie<UserSession>("X-SESSION-TOKEN", directorySessionStorage(File("sessions"), cached = false)) {
            cookie.path = "/"
            cookie.httpOnly = true
            cookie.extensions["SameSite"] = "strict"
        }
    }

    val sessionTokenPlugin = createApplicationPlugin(name = "SessionTokenPlugin") {
        onCall { call ->
            val session = call.sessions.get<UserSession>()

            if (session === null) {
                call.sessions.set(UserSession(null))
            }
        }
    }

    install(sessionTokenPlugin)
}
