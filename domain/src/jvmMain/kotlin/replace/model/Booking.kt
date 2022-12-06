package replace.model

import kotlinx.serialization.Serializable

@Serializable
data class Booking(
    val bookedEntities: List<BookableEntity>,
) : ObjectWithId()
