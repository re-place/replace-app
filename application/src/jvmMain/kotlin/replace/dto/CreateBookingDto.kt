package replace.dto

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class CreateBookingDto(
    val userId: String,
    val bookedEntityIds: List<String>,
    @Contextual val start: Instant,
    @Contextual val end: Instant
)
