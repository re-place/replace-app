package replace.dto

import kotlinx.serialization.Serializable

@Serializable
data class GetBookingDto(
    val start: String,
    val end: String?,
)
