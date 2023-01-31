package replace.http

import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.auth.OAuthServerSettings
import io.ktor.server.auth.authentication
import io.ktor.server.auth.oauth
import io.ktor.server.auth.session
import io.ktor.server.response.respondText
import io.ktor.server.routing.routing
import replace.http.controller.routeSession
import replace.model.UserSession

fun Application.authenticationModule() {
    authentication {
        oauth {
            urlProvider = { "/" }
            providerLookup = {
                OAuthServerSettings.OAuth2ServerSettings(
                    name = "microsoft",
                    authorizeUrl = "...",
                    accessTokenUrl = "...",
                    requestMethod = HttpMethod.Post,
                    clientId = "...",
                    clientSecret = "...",
                )
            }
        }
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
        routeSession()
    }
}
