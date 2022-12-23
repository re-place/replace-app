package replace.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class Booking(
    val bookedEntities: List<@Contextual ObjectId>,
) : ObjectWithId()
