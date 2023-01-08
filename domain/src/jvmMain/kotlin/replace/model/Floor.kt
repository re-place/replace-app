package replace.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class Floor(
    val name: String,
    @Contextual val siteId: ObjectId,
    @Contextual val planFileId: ObjectId? = null,
) : ObjectWithId()
