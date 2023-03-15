package replace.usecase.generator

import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.instant
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.uuid
import kotlinx.datetime.toKotlinInstant
import org.jetbrains.exposed.sql.transactions.transaction
import replace.dto.BookableEntityDto
import replace.dto.BookingDto
import replace.dto.CreateBookingDto
import replace.dto.UserDto
import replace.dto.toDto
import replace.model.Booking
import replace.model.User

fun ReplaceArb.booking(
    userArb: Arb<User> = ReplaceArb.user(),
): Arb<Booking> = arbitrary {
    val start = Arb.instant().bind().toKotlinInstant()
    val end = Arb.instant().bind().toKotlinInstant()
    val user = userArb.bind()
    transaction {
        Booking.new {
            this.start = start
            this.end = end
            this.user = user
        }
    }
}

fun ReplaceArb.bookingDto(
    userArb: Arb<UserDto> = ReplaceArb.userDto(),
    bookedEntitiesArb: Arb<List<BookableEntityDto>> = Arb.list(ReplaceArb.bookableEntity().map { it.toDto() }),
): Arb<BookingDto> = arbitrary {
    val id = Arb.uuid().bind().toString()
    val user = userArb.bind()
    val bookedEntities = bookedEntitiesArb.bind()
    val start = Arb.instant().bind().toKotlinInstant().toString()
    val end = Arb.instant().bind().toKotlinInstant().toString()
    BookingDto(id, user.id, user, bookedEntities, start, end)
}

fun ReplaceArb.bookingCreateDto(
    bookedEntitiesArb: Arb<List<String>> = Arb.list(ReplaceArb.bookableEntity().map { it.id.toString() }),
): Arb<CreateBookingDto> = arbitrary {
    val bookedEntities = bookedEntitiesArb.bind()
    val start = Arb.instant().bind().toKotlinInstant().toString()
    val end = Arb.instant().bind().toKotlinInstant().toString()
    CreateBookingDto(bookedEntities, start, end)
}
