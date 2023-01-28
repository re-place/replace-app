package replace.usecase.booking

import org.jetbrains.exposed.sql.transactions.transaction
import replace.dto.BookingDto
import replace.dto.GetBookingDto
import replace.dto.toDto
import replace.model.Booking

object GetBookingUseCase {
    suspend fun execute(
        getBookingDto: GetBookingDto,
    ): BookingDto {
        return transaction {

            val booking = Booking.all().first()
            /*val bookedEntities = BookableEntity.forEntityIds(getBookingDto.bookedEntityIds.map { EntityID(it, BookableEntities) })

            val booking = Booking.get(userId.toString()) {
                start = Instant.parse(getBookingDto.start)
                end = Instant.parse(getBookingDto.end)
                this.userId =
                bookedEntities = newBookedEntities
            }*/

            booking!!.toDto()
        }
    }
}
