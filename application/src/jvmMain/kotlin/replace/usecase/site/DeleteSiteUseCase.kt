package replace.usecase.site

import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import replace.model.Site
import replace.usecase.floor.DeleteFloorUseCase

object DeleteSiteUseCase {

    suspend fun execute(
        id: String
    ) {
        return transaction {
            val site = Site.findById(id)
            val floors = site?.floors?.forEach{
                floor -> print("a")// DeleteFloorUseCase.execute(floor.id.value)
            }

        }
    }
}
