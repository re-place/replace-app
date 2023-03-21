package replace.usecase.generator

import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.uuid
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
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
    val start = Arb.timeStamp().filter { it > Clock.System.now() }.bind()
    val end = Arb.timeStamp().filter { it > start }.bind()
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
    val start = Arb.timeStamp().filter { it > Clock.System.now() }.bind()
    val end = Arb.timeStamp().filter { it > start }.bind().toString()
    BookingDto(id, user.id, user, bookedEntities, start.toString(), end)
}

fun ReplaceArb.bookingCreateDto(
    bookedEntitiesArb: Arb<List<String>> = Arb.list(ReplaceArb.bookableEntity().map { it.id.toString() }, 1..4),
    startArb: Arb<Instant> = Arb.timeStamp().filter { it > Clock.System.now() },
    endArb: (startActual: Instant) -> Arb<Instant> = { startActual -> Arb.timeStamp().filter { it > startActual } },
): Arb<CreateBookingDto> = arbitrary {
    val bookedEntities = bookedEntitiesArb.bind()
    val start = startArb.bind()
    val end = endArb(start).bind().toString()
    CreateBookingDto(bookedEntities, start.toString(), end)
}
