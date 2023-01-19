package replace.dto

import java.time.LocalDateTime
import kotlin.reflect.KProperty1
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import replace.model.Booking

@Serializable
data class BookingDto(
    override val id: String,
    val userId: String,
    val user: UserDto? = null,
    val bookedEntities: List<BookableEntityDto>? = null,
    @Contextual val start: LocalDateTime,
    @Contextual val end: LocalDateTime
) : ModelDto

fun Booking.toDto(with: List<KProperty1<Booking, *>> = emptyList()): BookingDto {

    val bookedEntities = if (with.contains(Booking::bookedEntities)) {
        bookedEntities.map { it.toDto() }
    } else {
        null
    }

    val user = if (with.contains(Booking::user)) {
        user.toDto()
    } else {
        null
    }

    return BookingDto(
        id = id.value,
        userId = userId.value,
        start = start,
        end = end,
        bookedEntities = bookedEntities,
        user = user,
    )
}
