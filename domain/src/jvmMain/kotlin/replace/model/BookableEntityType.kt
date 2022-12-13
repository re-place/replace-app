package replace.model

import kotlinx.serialization.Serializable

@Serializable
data class BookableEntityType(
    val name: String,
) : ObjectWithId()
