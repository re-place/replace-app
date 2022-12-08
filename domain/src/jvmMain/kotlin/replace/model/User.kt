package replace.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val username: String,
    val password: String,
    val firstName: String,
    val lastName: String,
) : ObjectWithId()
