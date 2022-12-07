package replace.model

import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset

@Serializable
data class LoginSession(
    val userId: String,
    val userName: String,
    val startUtcFormatted: String = OffsetDateTime.now(ZoneOffset.UTC).toString(),
)

val LoginSession.startUtc: Instant
    get() = OffsetDateTime.parse(startUtcFormatted).toInstant()

fun User.createSession(): LoginSession = LoginSession(userId = _id.toString(), userName = userName)
