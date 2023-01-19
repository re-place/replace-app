/*
 *   ESATT - https://www.github.com/alexstaeding/ESATT
 *   Copyright (C) 2021 Bachelor-Praktikum Gruppe 20
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package replace.http

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
import org.jetbrains.exposed.sql.transactions.transaction
import replace.dto.LoginRequest
import replace.dto.toDto
import replace.model.User
import replace.model.UserSession
import replace.model.Users
import replace.model.createSession

fun Route.routeAuthentication() {
    get("/api/current-user") {
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
    post("/api/logout") {
        call.sessions.clear<UserSession>()
        call.respond(HttpStatusCode.OK)
    }
    post("/api/login") {
        if (call.sessions.get<UserSession>()?.userId != null) {
            call.respondText("Already authenticated", status = HttpStatusCode.BadRequest)
            return@post
        }

        val user = try {
            call.receive<LoginRequest>()
        } catch (e: Exception) {
            return@post call.respondText(
                "Could not sign in: ${e.message} caused by ${e.cause?.message}",
                status = HttpStatusCode.BadRequest
            )
        }
        // TODO: Replace with OAuth

        val userFromDb = transaction {
            User.find { Users.username eq user.username }.firstOrNull()
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
