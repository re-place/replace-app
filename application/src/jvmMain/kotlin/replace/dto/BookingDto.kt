package replace.dto

import java.time.LocalDateTime
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import replace.model.Booking
import replace.model.Bookings.user_id

@Serializable
data class BookingDto(
    override val id: String? = null,
    val userId: String,
    @Contextual val start: LocalDateTime,
    @Contextual val end: LocalDateTime,
) : Dto

fun Booking.toDto() = BookingDto(
    id = id.value,
    userId = userId.value,
    start = start,
    end = end
)
