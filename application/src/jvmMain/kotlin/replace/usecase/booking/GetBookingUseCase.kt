package replace.usecase.booking

import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.javatime.timestampLiteral
import org.jetbrains.exposed.sql.not
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.transactions.transaction
import replace.dto.BookingDto
import replace.dto.toDto
import replace.model.Booking
import replace.model.Bookings
import java.time.Instant

object GetBookingUseCase {
    suspend fun execute(
        start: String?,
        end: String?,
        userId: String?,
    ): List<BookingDto> {
        val startInst = Instant.parse(start)
        var endInst: Instant? = null
        if (end != null) {
            endInst = Instant.parse(end)
        }

        val bookings = transaction {
            Booking.find {
                if (endInst == null) {
                    (Bookings.end greaterEq timestampLiteral(startInst)) and (Bookings.user_id eq userId)
                } else {
                    not((Bookings.end lessEq timestampLiteral(startInst)) or (Bookings.start greater timestampLiteral(endInst)))
                }
            }.orderBy(Bookings.start to SortOrder.ASC).with(Booking::bookedEntities).map { it.toDto(listOf(Booking::bookedEntities)) }
        }

        return bookings
    }
}
