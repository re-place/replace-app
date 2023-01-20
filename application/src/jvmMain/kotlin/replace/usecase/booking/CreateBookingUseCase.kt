package replace.usecase.booking

import kotlinx.datetime.Instant
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.transactions.transaction
import replace.dto.BookingDto
import replace.dto.CreateBookingDto
import replace.dto.toDto
import replace.model.BookableEntities
import replace.model.BookableEntity
import replace.model.Booking
import replace.model.Users

object CreateBookingUseCase {
    suspend fun execute(
        createBookingDto: CreateBookingDto,
        userId: String
    ): BookingDto {
        return transaction {
            if (createBookingDto.bookedEntityIds.isEmpty()) {
                throw IllegalArgumentException("BookedEntities must not be empty")
            }

            val newBookedEntities = BookableEntity.forEntityIds(createBookingDto.bookedEntityIds.map { EntityID(it, BookableEntities) })

            val booking = Booking.new {
                start = Instant.parse(createBookingDto.start)
                end = Instant.parse(createBookingDto.end)
                this.userId = EntityID(userId, Users)
                bookedEntities = newBookedEntities
            }

            booking.toDto()
        }
    }
}
