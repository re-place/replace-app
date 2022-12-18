package replace.model

import org.bson.types.ObjectId

interface Floor : ObjectWithMaybeId {
    val name: String
    val siteId: ObjectId
}

interface FloorWithId : Floor, ObjectWithId

fun Floor.assertId(): FloorWithId = when (this) {
    is FloorWithId -> this
    else -> FloorWithIdImpl(this)
}

private class FloorWithIdImpl(delegate: Floor) : FloorWithId, Floor by delegate {
    override val id: ObjectId = checkNotNull(delegate.id) { "Floor ID is null" }
}
