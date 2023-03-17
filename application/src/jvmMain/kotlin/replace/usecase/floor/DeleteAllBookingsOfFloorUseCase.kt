package replace.usecase.floor

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import replace.model.BookedEntities

object DeleteAllBookingsOfFloorUseCase {
    suspend fun execute(
        bookableEntityId: String,
    ) {
        return newSuspendedTransaction {
            BookedEntities.deleteWhere { bookable_entity_id eq bookableEntityId }
        }
    }
}
