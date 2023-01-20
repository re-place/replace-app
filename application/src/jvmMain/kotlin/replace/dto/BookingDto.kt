package replace.dto

import kotlinx.serialization.Serializable
import replace.model.Booking
import kotlin.reflect.KProperty1

@Serializable
data class BookingDto(
    override val id: String,
    val userId: String,
    val user: UserDto? = null,
    val bookedEntities: List<BookableEntityDto>? = null,
    val start: String,
    val end: String
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
        start = start.toString(),
        end = end.toString(),
        bookedEntities = bookedEntities,
        user = user,
    )
}
