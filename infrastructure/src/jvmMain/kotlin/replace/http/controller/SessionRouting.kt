package replace.http.controller

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.sessions.clear
import io.ktor.server.sessions.get
import io.ktor.server.sessions.sessions
import io.ktor.server.sessions.set
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import replace.dto.toDto
import replace.model.User
import replace.model.UserSession
import replace.model.Users
import replace.model.createSession
import replace.usecase.session.dto.LoginRequest

fun Route.routeSession() {
    get("/api/session/current-user") {
        val session = call.sessions.get<UserSession>()
        val userId = session?.userId
        if (userId === null) {
            call.respondText("Not authenticated", status = HttpStatusCode.Unauthorized)
            return@get
        }

        val user = transaction { User.findById(userId) }

        if (user === null) {
            call.respondText("Not authenticated", status = HttpStatusCode.Unauthorized)
            return@get
        }

        call.respond(user.toDto())
    }
    post("/api/session/logout") {
        call.sessions.clear<UserSession>()
        call.respond(HttpStatusCode.OK)
    }
    post("/api/session/login") {
        if (call.sessions.get<UserSession>()?.userId != null) {
            call.respondText("Already authenticated", status = HttpStatusCode.BadRequest)
            return@post
        }

        val user = try {
            call.receive<LoginRequest>()
        } catch (e: Exception) {
            return@post call.respondText(
                "Could not sign in ${e::class.simpleName}: ${e.message}\n${e.cause?.stackTraceToString()}",
                status = HttpStatusCode.BadRequest
            )
        }
        // TODO: Replace with OAuth

        val userFromDb = transaction {
            User.find { Users.username eq user.username and(Users.password eq user.password) }.firstOrNull()
        }

        if (userFromDb === null) {
            call.respondText("Could not sign in: User not found", status = HttpStatusCode.BadRequest)
            return@post
        }
        call.sessions.clear("X-SESSION-TOKEN")
        call.sessions.set(userFromDb.createSession())
        call.respond(userFromDb.toDto())
    }
}
