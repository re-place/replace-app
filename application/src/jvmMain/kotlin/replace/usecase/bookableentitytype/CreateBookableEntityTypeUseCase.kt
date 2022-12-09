package replace.usecase.bookableentitytype

import replace.datastore.Repository
import replace.dto.BookableEntityTypeDto
import replace.dto.BookingDto
import replace.model.Booking

object CreateBookableEntityTypeUseCase {

    suspend fun execute(
        bookableEntityTypeDto: BookableEntityTypeDto,
        bookingRepository: Repository<Booking>,
    ): BookingDto {
        TODO("Check if already exists")
    }
}
