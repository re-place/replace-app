package replace.usecase.booking

import org.jetbrains.exposed.sql.transactions.transaction
import replace.model.Booking
import replace.model.Bookings

object DeleteBookingUseCase {
    suspend fun execute(
        bookingId: String,
        currentUserId: String,
    ) {
        return transaction {
            val booking = Booking.find {
                Bookings.id eq bookingId
                Bookings.user_id eq currentUserId
            }.firstOrNull()

            booking?.delete()
        }
    }
}
