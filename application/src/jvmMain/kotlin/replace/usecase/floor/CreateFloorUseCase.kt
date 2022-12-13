package replace.usecase.floor

import org.bson.types.ObjectId
import replace.datastore.FloorRepository
import replace.datastore.SiteRepository
import replace.dto.FloorDto
import replace.dto.toDto
import replace.model.Floor

object CreateFloorUseCase {
    suspend fun execute(
        floorDto: FloorDto,
        floorRepository: FloorRepository,
        siteRepository: SiteRepository,
    ): FloorDto {
        val name = floorDto.name
        val siteId = ObjectId(floorDto.siteId)
        val site = siteRepository.findOneById(siteId)
        checkNotNull(site) { "Site with id $siteId not found" }
        val floor = Floor(name, siteId)
        val insertedFloor = floorRepository.insertOne(floor)
        checkNotNull(insertedFloor) { "Could not insert BookableEntity" }
        return insertedFloor.toDto()
    }
}
