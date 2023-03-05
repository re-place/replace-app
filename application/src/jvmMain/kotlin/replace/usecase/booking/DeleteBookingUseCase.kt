package replace.usecase.booking

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import replace.model.BookedEntities
import replace.model.Bookings

object DeleteBookingUseCase {
    suspend fun execute(
        bookingId: String,
        currentUserId: String
    ) {
        return transaction {
            BookedEntities.deleteWhere { BookedEntities.booking_id eq bookingId }
            Bookings.deleteWhere { Bookings.id eq bookingId and (Bookings.user_id eq currentUserId) }
        }
    }
}
