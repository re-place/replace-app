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
        val locationId = ObjectId(floorDto.locationId)
        val site = siteRepository.findOneById(locationId)
        checkNotNull(site) { "Location ID not found" }
        val floor = Floor(name, locationId)
        val insertedFloor = floorRepository.insertOne(floor)
        checkNotNull(insertedFloor) { "Could not insert BookableEntity" }
        return insertedFloor.toDto()
    }
}
