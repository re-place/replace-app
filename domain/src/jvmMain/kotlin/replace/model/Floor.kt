package replace.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class Floor(
    val name: String,
    @Contextual val officeId: ObjectId,
) : ObjectWithId()
