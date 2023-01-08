package replace.usecase.booking

import kotlinx.datetime.Instant
import org.bson.types.ObjectId
import replace.datastore.BookableEntityRepository
import replace.datastore.Repository
import replace.dto.BookingDto
import replace.dto.toDto
import replace.model.Booking

object CreateBookingUseCase {
    suspend fun execute(
        bookingDto: BookingDto,
        bookingRepository: Repository<Booking>,
        bookableEntityRepository: BookableEntityRepository,
    ): BookingDto {
        val bookedEntities = bookingDto.bookedEntities.map { ObjectId(it) }
        val currentMoment = bookingDto.currentMoment

        // ensure that each booked entity exists
        // TODO: Clean up tree if parent + child are booked?

        bookedEntities.forEach {
            bookableEntityRepository.findOneById(it) ?: throw IllegalStateException("Entity with id $it does not exist")
        }
        val insertedBooking = bookingRepository.insertOne(Booking(bookedEntities, Instant.parse(currentMoment)))
        checkNotNull(insertedBooking) { "Could not insert booking" }
        return insertedBooking.toDto()
    }
}
