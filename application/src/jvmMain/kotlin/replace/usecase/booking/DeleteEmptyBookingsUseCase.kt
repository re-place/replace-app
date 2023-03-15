package replace.usecase.booking

import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import replace.model.BookedEntities
import replace.model.Bookings

object DeleteEmptyBookingsUseCase {
    suspend fun execute(): Int {
        return newSuspendedTransaction {
            val bookingsToDelete = Bookings
                .join(BookedEntities, JoinType.LEFT, Bookings.id, BookedEntities.booking_id)
                .slice(Bookings.id)
                .select(BookedEntities.id eq null)
                .toList()
                .map { it[Bookings.id] }

            Bookings.deleteWhere { Bookings.id inList bookingsToDelete }
        }
    }
}
