package replace.usecase.floor

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import replace.datastore.FileStorage
import replace.model.BookableEntities
import replace.model.BookableEntity
import replace.model.Floor
import replace.usecase.file.DeleteFileUseCase

object DeleteFloorUseCase {
    suspend fun execute(
        floorId: String,
        fileStorage: FileStorage
    ) {
        val floor = transaction {
            Floor.findById(floorId)
        } ?: return

        val bookableEntities = transaction {
            BookableEntity.find(BookableEntities.floor_id eq floorId).toList()
        }

        bookableEntities.toList().forEach {
            DeleteAllBookingsOfFloorUseCase.execute(it.id.value)
        }

        transaction {
            BookableEntities.deleteWhere { BookableEntities.floor_id eq floorId }
        }

        transaction {
            floor.delete()
        }

        newSuspendedTransaction {
            floor.planFileId?.let {
                DeleteFileUseCase.execute(it.value, fileStorage)
            }
        }
    }
}
