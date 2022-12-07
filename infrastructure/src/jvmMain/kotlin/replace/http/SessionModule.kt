package replace.http

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import replace.datastore.UserRepository
import replace.model.UserSession
import java.io.File

fun Application.sessionModule() {
    install(Sessions) {
        header<UserSession>("SESSION_TOKEN", directorySessionStorage(File(".sessions"), cached = false))
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
