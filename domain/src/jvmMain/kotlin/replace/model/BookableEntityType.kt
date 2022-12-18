package replace.model

import org.bson.types.ObjectId

interface BookableEntityType : ObjectWithMaybeId {
    val name: String
}

interface BookableEntityTypeWithId : BookableEntityType, ObjectWithId

fun BookableEntityType.assertId(): BookableEntityTypeWithId = when (this) {
    is BookableEntityTypeWithId -> this
    else -> BookableEntityTypeWithIdImpl(this)
}

private class BookableEntityTypeWithIdImpl(delegate: BookableEntityType) : BookableEntityTypeWithId, BookableEntityType by delegate {
    override val id: ObjectId = checkNotNull(delegate.id) { "BookableEntityType ID is null" }
}
