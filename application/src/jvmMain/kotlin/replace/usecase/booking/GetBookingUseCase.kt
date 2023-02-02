package replace.usecase.booking

import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.javatime.timestampLiteral
import org.jetbrains.exposed.sql.selectAll
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
        val startInst = start?.let { Instant.parse(it) }
        val endInst = end?.let { Instant.parse(it) }

        val bookingDtos = transaction {

            val query = Bookings.selectAll()

            startInst?.let {
                query.andWhere { Bookings.end greaterEq timestampLiteral(it) }
            }

            endInst?.let {
                query.andWhere { Bookings.start lessEq timestampLiteral(it) }
            }

            userId?.let {
                query.andWhere { Bookings.user_id eq it }
            }

            val bookings = query.where?.let { Booking.find { it } } ?: Booking.all()

            bookings.orderBy(Bookings.start to SortOrder.ASC).with(Booking::bookedEntities).map { it.toDto(listOf(Booking::bookedEntities)) }
        }

        return bookingDtos
    }
}
