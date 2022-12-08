package replace.model

import io.ktor.server.auth.Principal
import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset

@Serializable
data class UserSession(
    val userId: String?,
    val startUtcFormatted: String = OffsetDateTime.now(ZoneOffset.UTC).toString(),
) : Principal

val UserSession.startUtc: Instant
    get() = OffsetDateTime.parse(startUtcFormatted).toInstant()

fun User.createSession(): UserSession = UserSession(userId = _id.toString())
