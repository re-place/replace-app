package replace.model

import kotlinx.serialization.Serializable

@Serializable
data class BookableEntity(
    val parentId: String,
) : ObjectWithId()
