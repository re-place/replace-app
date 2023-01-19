package replace.dto

import java.time.LocalDateTime
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class UpdateBookingDto(
    val id: String,
    val userId: String,
    val bookedEntityIds: List<String>,
    @Contextual val start: LocalDateTime,
    @Contextual val end: LocalDateTime
)
