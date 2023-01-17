package replace.usecase.site

import replace.datastore.SiteRepository
import replace.dto.SiteDto
import replace.dto.toDto

object CreateSiteUseCase {
    suspend fun execute(
        siteDto: SiteDto,
        siteRepository: SiteRepository,
    ): SiteDto {
        val site = Site(siteDto.name)
        val insertedSite = siteRepository.insertOne(site)
        checkNotNull(insertedSite) { "Could not insert Site" }
        return insertedSite.toDto()
    }
}
