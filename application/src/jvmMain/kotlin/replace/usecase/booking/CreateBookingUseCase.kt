package replace.usecase.booking

import org.jetbrains.exposed.dao.id.EntityID
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
    ): BookingDto {
        if (createBookingDto.bookedEntityIds.isEmpty()) {
            throw IllegalArgumentException("BookedEntities must not be empty")
        }

        val newBookedEntities = BookableEntity.forEntityIds(createBookingDto.bookedEntityIds.map { EntityID(it, BookableEntities) })

        val booking = Booking.new {
            start = createBookingDto.start
            end = createBookingDto.end
            userId = EntityID(createBookingDto.userId, Users)
            bookedEntities = newBookedEntities
        }

        return booking.toDto()
    }
}
