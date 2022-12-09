package replace.model

import org.bson.types.ObjectId

sealed class ObjectWithId {
    var _id: ObjectId? = null
}
