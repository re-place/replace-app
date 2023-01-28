package replace.dto

import kotlinx.serialization.Serializable

@Serializable
data class GetBookingByDateDto(
    val start: String,
    val end: String?,
)
