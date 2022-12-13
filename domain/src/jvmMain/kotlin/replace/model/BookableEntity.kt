package replace.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class BookableEntity(
    val name: String,
    val type: BookableEntityType,
    @Contextual val parentId: ObjectId? = null,
    @Contextual val floorId: ObjectId,
) : ObjectWithId()
