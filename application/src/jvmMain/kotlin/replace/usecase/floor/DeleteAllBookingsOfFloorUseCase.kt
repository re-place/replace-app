package replace.usecase.floor

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import replace.model.BookedEntities
import replace.model.Bookings

object DeleteAllBookingsOfFloorUseCase {
    suspend fun execute(
        bookableEntityId: String
    ) {
        return transaction {
            Bookings.deleteWhere { Bookings.id eq bookableEntityId }
            BookedEntities.deleteWhere { bookable_entity_id eq id }
        }
    }
}
