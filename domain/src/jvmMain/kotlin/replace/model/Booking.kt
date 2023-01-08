package replace.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class Booking(
    val bookedEntities: List<@Contextual ObjectId>,
    val startDateTime: Instant,
) : ObjectWithId()
