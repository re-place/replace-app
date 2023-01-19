package replace.dto

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class CreateBookingDto(
    val userId: String,
    val bookedEntityIds: List<String>,
    @Contextual val start: LocalDateTime,
    @Contextual val end: LocalDateTime
)
