package replace.usecase.booking

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import replace.model.BookedEntities
import replace.model.Bookings

object DeleteBookingUseCase {
    suspend fun execute(
        bookingId: String,
        currentUserId: String,
    ) {
        return newSuspendedTransaction {
            BookedEntities.deleteWhere { booking_id eq bookingId }
            Bookings.deleteWhere { (Bookings.id eq bookingId) and (user_id eq currentUserId) }
        }
    }
}
