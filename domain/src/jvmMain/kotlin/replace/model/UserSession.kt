package replace.model

import io.ktor.server.auth.Principal
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class UserSession(
    val userId: String?,
    val startTimeString: String = Clock.System.now().toString(),
) : Principal

val UserSession.start: Instant
    get() = Instant.parse(startTimeString)

fun User.createSession(): UserSession = UserSession(userId = id.toString())
