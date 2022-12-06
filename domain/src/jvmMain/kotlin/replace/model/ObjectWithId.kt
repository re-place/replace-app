package replace.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
sealed class ObjectWithId {
    @Contextual
    var _id: ObjectId? = null
}
