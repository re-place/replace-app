package replace.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateBookingDto(
    val bookedEntityIds: List<String>,
    val start: String,
    val end: String
)
