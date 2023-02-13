package replace.http

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.auth.OAuthAccessTokenResponse
import io.ktor.server.auth.OAuthServerSettings
import io.ktor.server.auth.Principal
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.authentication
import io.ktor.server.auth.oauth
import io.ktor.server.auth.principal
import io.ktor.server.auth.session
import io.ktor.server.config.tryGetString
import io.ktor.server.response.respond
import io.ktor.server.response.respondRedirect
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import io.ktor.server.sessions.Sessions
import io.ktor.server.sessions.clear
import io.ktor.server.sessions.cookie
import io.ktor.server.sessions.directorySessionStorage
import io.ktor.server.sessions.get
import io.ktor.server.sessions.maxAge
import io.ktor.server.sessions.sessions
import io.ktor.server.sessions.set
import io.ktor.util.pipeline.PipelineContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.transactions.transaction
import replace.dto.toDto
import replace.model.User
import replace.model.Users
import java.io.File
import kotlin.time.Duration.Companion.hours

fun Application.authenticationModule() {
    val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(
                Json {
                    encodeDefaults = false
                    ignoreUnknownKeys = true
                }
            )
        }
    }
    install(Sessions) {
        cookie<UserSession>("X-SESSION-TOKEN", directorySessionStorage(File("sessions"), cached = false)) {
            cookie.path = "/"
            cookie.httpOnly = true
            cookie.maxAge = 1.hours
        }
    }
    authentication {
        session<UserSession>("internal-session") {
            validate { it }
            challenge {
                call.respondText("Not authenticated", status = HttpStatusCode.Unauthorized)
            }
        }
        oauth("microsoft-oauth") {

            val callback = this@authenticationModule.environment.config.tryGetString("ktor.oauth.callback")
                ?: throw IllegalStateException("Missing OAuth callback URL")
            urlProvider = { callback }

            client = httpClient

            if (this@authenticationModule.environment.developmentMode) {
                providerLookup = {
                    OAuthServerSettings.OAuth2ServerSettings(
                        name = "mock lab",
                        authorizeUrl = "https://oauth.mocklab.io/oauth/authorize",
                        accessTokenUrl = "https://oauth.mocklab.io/oauth/token",
                        requestMethod = HttpMethod.Post,
                        clientId = "test-client-id",
                        clientSecret = "test-client-secret",
                        defaultScopes = listOf("email", "offline_access", "openid", "profile", "User.Read"),
                    )
                }

                return@oauth
            }

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
                    defaultScopes = listOf("email", "offline_access", "openid", "profile", "User.Read"),
                )
            }
        }
    }

    routing {
        get("/api/session/current-user") {
            withUser { call.respond(it.toDto()) }
        }
        post("/api/session/logout") {
            call.sessions.clear<UserSession>()
            call.respondText("Logged out")
        }
        authenticate("microsoft-oauth") {
            get("/api/session/login") {
                // Redirects to 'authorizeUrl' automatically
            }
            get("/api/session/callback") {
                val principal: OAuthAccessTokenResponse.OAuth2 = checkNotNull(call.principal()) { "No principal" }

                val userInfo = if (this@authenticationModule.developmentMode) {
                    MicrosoftUserInfo("", "Max Mustermann", "Max", "Mustermann", "max@musterman@example.com")
                } else {
                    httpClient.get("https://graph.microsoft.com/v1.0/me") {
                        header("Authorization", "Bearer ${principal.accessToken}")
                    }.body<MicrosoftUserInfo>()
                }

                // find user in db

                val user = transaction {
                    User.find { Users.email eq userInfo.email }.firstOrNull()
                        ?.also { println("Found user with email ${it.email} in DB") }
                } ?: transaction {
                    println("Creating user with email ${userInfo.email}")
                    User.new {
                        email = userInfo.email
                        firstname = userInfo.firstName
                        lastname = userInfo.lastName
                    }
                }

                val session = UserSession(
                    user.id.value,
                    checkNotNull(principal.state) { "No state" },
                    principal.accessToken,
                    userInfo.email
                )

                println("Creating session $session")
                call.sessions.set(session)
                call.respondRedirect("/reservation/my-bookings")
            }
        }
    }
}

suspend fun PipelineContext<Unit, ApplicationCall>.withUserSession(block: suspend (UserSession) -> Unit) {
    val session = call.sessions.get<UserSession>()
    if (session === null) {
        call.respondText("Not logged in", status = HttpStatusCode.Unauthorized)
    } else {
        block(session)
    }
}

suspend fun PipelineContext<Unit, ApplicationCall>.withUser(block: suspend (User) -> Unit) {
    withUserSession { session ->
        val user = transaction { User.find { Users.email eq session.email }.firstOrNull() }
        checkNotNull(user) { "Could not find user ${session.email} in DB" }
        block(user)
    }
}

@Serializable
data class UserSession(
    val userId: String,
    val state: String,
    val token: String,
    val email: String,
) : Principal

@Serializable
data class MicrosoftUserInfo(
    @SerialName("@odata.context") val dataContext: String,
    val displayName: String,
    @SerialName("givenName") val firstName: String,
    @SerialName("surname") val lastName: String,
    @SerialName("userPrincipalName") val email: String,
)
