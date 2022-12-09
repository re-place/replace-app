package replace.model

import org.bson.types.ObjectId

data class BookableEntity(
    val name: String,
    val type: BookableEntityType,
    val parentId: ObjectId? = null,
) : ObjectWithId()
