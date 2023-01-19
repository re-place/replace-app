package replace.usecase.site

import replace.dto.CreateSiteDto
import replace.dto.SiteDto
import replace.dto.toDto
import replace.model.Site

object CreateSiteUseCase {
    suspend fun execute(
        createSiteDto: CreateSiteDto,
    ): SiteDto {
        val site = Site.new {
            name = createSiteDto.name
        }

        return site.toDto()
    }
}
