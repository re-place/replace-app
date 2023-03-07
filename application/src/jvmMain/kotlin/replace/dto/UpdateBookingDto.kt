package replace.dto

import kotlinx.serialization.Serializable

@Serializable
data class UpdateBookingDto(
    val id: String,
    val userId: String,
    val bookedEntityIds: List<String>,
    val start: String,
    val end: String,
)
