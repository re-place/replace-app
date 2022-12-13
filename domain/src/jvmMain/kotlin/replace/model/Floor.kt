package replace.model

import org.bson.types.ObjectId

data class Floor(
    val name: String,
    val locationId: ObjectId,
) : ObjectWithId()
