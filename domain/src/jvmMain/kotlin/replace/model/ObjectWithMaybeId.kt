package replace.model

import kotlinx.serialization.SerialName
import org.bson.types.ObjectId

sealed interface ObjectWithMaybeId {
    @SerialName("_id")
    val id: ObjectId?
}

interface ObjectWithId : ObjectWithMaybeId {
    override val id: ObjectId
    fun copy(id: ObjectId): ObjectWithId
}
