package replace.model

import org.bson.types.ObjectId

data class Booking(
    val bookedEntities: List<ObjectId>,
) : ObjectWithId()
