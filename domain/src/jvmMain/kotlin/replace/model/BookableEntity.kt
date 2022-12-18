package replace.model

import org.bson.types.ObjectId

interface BookableEntity : ObjectWithMaybeId {
    val name: String
    val type: BookableEntityType
    val floorId: ObjectId
    val parentId: ObjectId?
}

interface BookableEntityWithId : BookableEntity, ObjectWithId {
    override val type: BookableEntityTypeWithId
}

fun BookableEntity.assertId(): BookableEntityWithId = when (this) {
    is BookableEntityWithId -> this
    else -> BookableEntityWithIdImpl(this)
}

private class BookableEntityWithIdImpl(delegate: BookableEntity) : BookableEntityWithId, BookableEntity by delegate {
    override val id: ObjectId = checkNotNull(delegate.id) { "BookableEntity ID is null" }
    override val type: BookableEntityTypeWithId = delegate.type.assertId()
}
