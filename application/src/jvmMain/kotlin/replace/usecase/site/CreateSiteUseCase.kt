package replace.usecase.site

import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import replace.dto.CreateSiteDto
import replace.dto.SiteDto
import replace.dto.toDto
import replace.model.Site

object CreateSiteUseCase {
    suspend fun execute(
        createSiteDto: CreateSiteDto,
    ): SiteDto {
        return newSuspendedTransaction {
            val site = Site.new {
                name = createSiteDto.name
            }

            site.toDto()
        }
    }
}
