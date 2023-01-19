package replace.dto

import java.time.LocalDateTime
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class CreateBookingDto(
    val userId: String,
    val bookedEntityIds: List<String>,
    @Contextual val start: LocalDateTime,
    @Contextual val end: LocalDateTime
)
