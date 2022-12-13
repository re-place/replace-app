package replace.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
sealed class ObjectWithId {
    @Contextual
    @SerialName("_id")
    var id: ObjectId? = null
}
