package replace.usecase.floor

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import replace.datastore.FileStorage
import replace.model.BookableEntities
import replace.model.BookableEntity
import replace.model.Floor
import replace.usecase.booking.DeleteEmptyBookingsUseCase
import replace.usecase.file.DeleteFileUseCase

object DeleteFloorUseCase {
    suspend fun execute(
        floorId: String,
        fileStorage: FileStorage,
    ) {
        val floor = newSuspendedTransaction {
            Floor.findById(floorId)
        } ?: return

        val bookableEntities = newSuspendedTransaction {
            BookableEntity.find(BookableEntities.floor_id eq floorId).toList()
        }

        bookableEntities.toList().forEach {
            DeleteAllBookingsOfFloorUseCase.execute(it.id.value)
        }

        newSuspendedTransaction {
            BookableEntities.deleteWhere { BookableEntities.floor_id eq floorId }
        }

        newSuspendedTransaction {
            floor.delete()
        }

        floor.planFileId?.let {
            DeleteFileUseCase.execute(it.value, fileStorage)
        }

        DeleteEmptyBookingsUseCase.execute()
    }
}
