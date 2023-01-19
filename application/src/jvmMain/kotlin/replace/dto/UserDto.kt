package replace.dto

import kotlin.reflect.KProperty1
import kotlinx.serialization.Serializable
import replace.model.User

@Serializable
class UserDto(
    override val id: String,
    val username: String,
    val firstName: String,
    val password: String,
    val lastName: String,
    val bookings: List<BookingDto>? = null,
): ModelDto

fun User.toDto(with: List<KProperty1<User, *>> = emptyList()): UserDto {
    val bookings = if (with.contains(User::bookings)) {
        bookings.map { it.toDto() }
    } else {
        null
    }

    return UserDto(
        id = id.value,
        username = username,
        password = password,
        firstName = firstName,
        lastName = lastName,
        bookings = bookings,
    )
}
