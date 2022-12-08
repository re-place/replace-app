package replace.model

import kotlinx.serialization.Serializable

@Serializable
data class Office(
    val name: String,
) : ObjectWithId()
