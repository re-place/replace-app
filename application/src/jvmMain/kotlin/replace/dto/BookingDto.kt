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
) : Dto

fun Booking.toDto() = BookingDto(
    id = id?.toHexString(),
    bookedEntities = bookedEntities.map { it.toHexString() },
    startDateTime = startDateTime.toString(),
)

fun BookingDto.toModel() = Booking(bookedEntities.map { ObjectId(it) }, Instant.parse(startDateTime))
