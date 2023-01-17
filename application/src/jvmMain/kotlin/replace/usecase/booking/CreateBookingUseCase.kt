package replace.usecase.booking

import org.bson.types.ObjectId
import replace.datastore.Repository
import replace.dto.BookingDto
import replace.dto.toDto
import replace.model.Bookings

object CreateBookingUseCase {
    suspend fun execute(
        bookingDto: BookingDto,
        bookingRepository: Repository<Bookings>,
    ): BookingDto {
        val bookedEntities = bookingDto.bookedEntities.map { ObjectId(it) }

        // ensure that each booked entity exists
        // TODO: Clean up tree if parent + child are booked?

        bookedEntities.forEach {
            bookingRepository.findOneById(it) ?: throw IllegalStateException("Entity with id $it does not exist")
        }
        val insertedBooking = bookingRepository.insertOne(Booking(bookedEntities))
        checkNotNull(insertedBooking) { "Could not insert booking" }
        return insertedBooking.toDto()
    }
}
