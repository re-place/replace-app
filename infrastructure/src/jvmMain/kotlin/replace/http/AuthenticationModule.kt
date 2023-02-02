package replace.http

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.http.HttpMethod
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.auth.OAuthAccessTokenResponse
import io.ktor.server.auth.OAuthServerSettings
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.authentication
import io.ktor.server.auth.oauth
import io.ktor.server.auth.principal
import io.ktor.server.config.tryGetString
import io.ktor.server.response.respondRedirect
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.sessions.Sessions
import io.ktor.server.sessions.cookie
import io.ktor.server.sessions.directorySessionStorage
import io.ktor.server.sessions.get
import io.ktor.server.sessions.sessions
import io.ktor.server.sessions.set
import kotlinx.serialization.Serializable
import java.io.File

fun Application.authenticationModule() {
    val httpClient = HttpClient(CIO)
    install(Sessions) {
        cookie<UserSession>("X-SESSION-TOKEN", directorySessionStorage(File("sessions"), cached = false)) {
            cookie.path = "/"
            cookie.httpOnly = true
        }
    }
    authentication {
        oauth {
            urlProvider = { "http://localhost:4200/api/session/callback" }
            providerLookup = {
                OAuthServerSettings.OAuth2ServerSettings(
                    name = "microsoft",
                    authorizeUrl = "https://login.microsoftonline.com/common/oauth2/v2.0/authorize",
                    accessTokenUrl = "https://login.microsoftonline.com/common/oauth2/v2.0/token",
                    requestMethod = HttpMethod.Post,
                    clientId = this@authenticationModule.environment.config.tryGetString("ktor.oauth.clientId")
                        ?: throw IllegalStateException("Missing OAuth client ID"),
                    clientSecret = this@authenticationModule.environment.config.tryGetString("ktor.oauth.clientSecret")
                        ?: throw IllegalStateException("Missing OAuth client secret"),
                    defaultScopes = listOf("profile", "openid", "email", "offline_access", "AccessReview.Read.All"),
                )
            }
            client = httpClient
        }
    }
    routing {
        get("/api/session/current-user") {
            val oauthSession = call.sessions.get<UserSession>()
            if (oauthSession === null) {
                call.respondText("Not logged in")
            } else {
                call.respondText("Logged in as $oauthSession")
            }
        }
        authenticate {
            get("/api/session/login") {
                // Redirects to 'authorizeUrl' automatically
            }
            get("/api/session/callback") {
                val principal: OAuthAccessTokenResponse.OAuth2 = checkNotNull(call.principal()) { "No principal" }
                call.sessions.set(
                    UserSession(
                        checkNotNull(principal.state) { "No state" },
                        principal.accessToken
                    )
                )
                println("Setting session token to ${principal.accessToken}")
                call.respondRedirect("http://localhost:4200/dashboard")
            }
        }
    }
}

@Serializable
data class UserSession(
    val state: String,
    val token: String,
)
