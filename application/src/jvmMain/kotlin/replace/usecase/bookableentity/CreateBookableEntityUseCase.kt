package replace.usecase.bookableentity

import org.bson.types.ObjectId
import replace.datastore.Repository
import replace.dto.BookingDto
import replace.dto.toDto
import replace.model.BookableEntity
import replace.model.Booking

object CreateBookableEntityUseCase {
    suspend fun execute(
        bookableEntityDto: BookableEntity,
        bookingRepository: Repository<Booking>,
    ): BookingDto {
        TODO("Check if parent exists etc")
    }
}
