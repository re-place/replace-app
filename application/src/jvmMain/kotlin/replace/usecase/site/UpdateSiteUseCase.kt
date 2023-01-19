package replace.usecase.site

import org.jetbrains.exposed.sql.transactions.transaction
import replace.dto.SiteDto
import replace.dto.UpdateSiteDto
import replace.dto.toDto
import replace.model.Site

object UpdateSiteUseCase {
    suspend fun execute(
        updateSiteDto: UpdateSiteDto,
    ): SiteDto {
        return transaction {
            val site = Site.findById(updateSiteDto.id)

            checkNotNull(site) { "Site with id ${updateSiteDto.id} not found" }

            site.name = updateSiteDto.name

            site.toDto()
        }
    }
}
