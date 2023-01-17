package replace.dto

import kotlinx.serialization.Serializable
import replace.model.User
import replace.model.Users

@Serializable
class UserDto(
    override val id: String? = null,
    val username: String,
    val firstName: String,
    val lastName: String,
) : Dto

fun User.toDto() = UserDto(
    id = id.value,
    username = username,
    firstName = firstName,
    lastName = lastName,
)
