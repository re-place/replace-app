package replace.dto

import kotlinx.serialization.Serializable
import replace.model.User
import kotlin.reflect.KProperty1

@Serializable
class UserDto(
    override val id: String,
    val email: String,
    val firstname: String,
    val lastname: String,
    val bookings: List<BookingDto>? = null,
) : ModelDto

suspend fun User.toDto(with: List<KProperty1<User, *>> = emptyList()): UserDto {
    val bookings = if (with.contains(User::bookings)) {
        bookings.map { it.toDto() }
    } else {
        null
    }

    return UserDto(
        id = id.value,
        email = email,
        firstname = firstname,
        lastname = lastname,
        bookings = bookings,
    )
}
