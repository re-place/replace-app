package replace.usecase.site

import replace.dto.SiteDto
import replace.dto.UpdateSiteDto
import replace.dto.toDto
import replace.model.Site

object UpdateSiteUseCase {
    suspend fun execute(
        updateSiteDto: UpdateSiteDto,
    ): SiteDto {

        val site = Site.findById(updateSiteDto.id)

        checkNotNull(site) { "Site with id ${updateSiteDto.id} not found" }

        site.name = updateSiteDto.name

        site.refresh()

        return site.toDto()
    }
}
