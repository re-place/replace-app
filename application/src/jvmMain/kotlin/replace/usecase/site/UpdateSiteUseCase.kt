package replace.usecase.site

import org.bson.types.ObjectId
import replace.datastore.SiteRepository
import replace.dto.SiteDto
import replace.dto.toDto
import replace.dto.toModel

object UpdateSiteUseCase {
    suspend fun execute(
        siteDto: SiteDto,
        siteRepository: SiteRepository,
    ): SiteDto {
        val siteId = ObjectId(siteDto.id)

        val updatedSite = siteRepository.updateOne(siteId, siteDto.toModel())

        checkNotNull(updatedSite) { "Could not update Site" }

        return updatedSite.toDto()
    }
}
