package replace.usecase.site

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import replace.datastore.FileStorage
import replace.model.Floor
import replace.model.Floors
import replace.model.Site
import replace.usecase.floor.DeleteFloorUseCase

object DeleteSiteUseCase {
    suspend fun execute(
        siteId: String,
        fileStorage: FileStorage,
    ) {
        val site = newSuspendedTransaction {
            Site.findById(siteId)
        } ?: return

        val floors = newSuspendedTransaction {
            Floor.find(Floors.site_id eq siteId).toList()
        }

        floors.forEach {
            DeleteFloorUseCase.execute(it.id.value, fileStorage)
        }

        newSuspendedTransaction {
            site.delete()
        }
    }
}
