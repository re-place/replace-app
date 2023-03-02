package replace.usecase.floor

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import replace.datastore.FileStorage
import replace.model.BookableEntities
import replace.model.BookableEntity
import replace.model.Floor

object DeleteFloorUseCase {
    suspend fun execute(
        floor_id: String,
        fileStorage: FileStorage
    ) {
        return transaction {
            val floor = Floor.findById(floor_id)

            checkNotNull(floor) { "Floor with id ${floor_id} not found" }


            val bookableEntities = BookableEntity.find(BookableEntities.floor_id eq floor_id)

            for( bookablentity in bookableEntities){
//                   DeleteAllBookingsOfFloorUseCase.execute(bookablentity.id.value)
                 print("##########")
                bookablentity.delete()
            }

            //Floor
            floor.delete()

            //PlanFile
           floor.planFileId?.let {
               //DeleteFileUseCase.execute(it.value, fileStorage )
               print("deletefile..")
           }
        }
    }
}
