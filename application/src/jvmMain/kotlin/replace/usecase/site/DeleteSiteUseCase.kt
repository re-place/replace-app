package replace.usecase.site

import org.jetbrains.exposed.sql.transactions.transaction
import replace.model.Site

object DeleteSiteUseCase {

    suspend fun execute(
        id: String
    ) {
        return transaction {
            val site = Site.findById(id)
            val floors = site?.floors?.forEach {
                floor ->
                print("a") // DeleteFloorUseCase.execute(floor.id.value)
            }
        }
    }
}
