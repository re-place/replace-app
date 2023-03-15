package replace.usecase.site

import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import replace.dto.SiteDto
import replace.dto.UpdateSiteDto
import replace.dto.toDto
import replace.model.Site

object UpdateSiteUseCase {
    suspend fun execute(
        updateSiteDto: UpdateSiteDto,
    ): SiteDto {
        return newSuspendedTransaction {
            val site = Site.findById(updateSiteDto.id)

            checkNotNull(site) { "Site with id ${updateSiteDto.id} not found" }

            site.name = updateSiteDto.name

            site.toDto()
        }
    }
}
