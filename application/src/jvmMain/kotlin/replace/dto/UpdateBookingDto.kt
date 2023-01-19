package replace.dto

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class UpdateBookingDto(
    val id: String,
    val userId: String,
    val bookedEntityIds: List<String>,
    @Contextual val start: LocalDateTime,
    @Contextual val end: LocalDateTime
)
