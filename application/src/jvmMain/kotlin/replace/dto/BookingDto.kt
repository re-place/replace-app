package replace.dto

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import replace.model.Booking

@Serializable
data class BookingDto(
    override val id: String? = null,
    val bookedEntities: List<String>,
    val startDateTime: String,
    val endDateTime: String,
) : Dto

fun Booking.toDto() = BookingDto(
    id = id?.toHexString(),
    bookedEntities = bookedEntities.map { it.toHexString() },
    startDateTime = startDateTime.toString(),
    endDateTime = startDateTime.toString(),
)

fun BookingDto.toModel() = Booking(bookedEntities.map { ObjectId(it) }, Instant.parse(startDateTime), Instant.parse(endDateTime))
