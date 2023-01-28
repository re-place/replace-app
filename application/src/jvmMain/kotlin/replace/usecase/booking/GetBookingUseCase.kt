package replace.usecase.booking

import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.javatime.timestampLiteral
import org.jetbrains.exposed.sql.not
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.transactions.transaction
import replace.dto.BookingDto
import replace.dto.GetBookingDto
import replace.dto.toDto
import replace.model.Booking
import replace.model.Bookings
import java.time.Instant

object GetBookingUseCase {
    suspend fun execute(
        getBookingDto: GetBookingDto,
        userId: String,
    ): List<BookingDto> {
        val start = Instant.parse(getBookingDto.start)
        var end: Instant? = null
        if (getBookingDto.end != null) {
            end = Instant.parse(getBookingDto.end)
        }

        val bookings = transaction {
            Booking.find {
                if (end == null) {
                    (Bookings.start greaterEq timestampLiteral(start)) and (Bookings.user_id eq userId)
                } else {
                    not((Bookings.end lessEq timestampLiteral(start)) or (Bookings.start greater timestampLiteral(end)))
                }
            }.map { it.toDto() }
        }

        return bookings
    }
}
