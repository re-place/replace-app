package replace.model

import kotlinx.serialization.Serializable

@Serializable
data class Site(
    val name: String,
) : ObjectWithId()
