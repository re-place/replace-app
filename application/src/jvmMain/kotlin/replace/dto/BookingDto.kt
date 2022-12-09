package replace.dto

import kotlinx.serialization.Serializable
import replace.model.Booking

@Serializable
data class BookingDto(
    override val id: String? = null,
    val bookedEntities: List<String>,
) : Dto

fun Booking.toDto() = BookingDto(
    id = _id?.toHexString(),
    bookedEntities = bookedEntities.map { it.toHexString() },
)
