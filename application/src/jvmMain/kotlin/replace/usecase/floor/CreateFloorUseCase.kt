package replace.usecase.floor

import org.bson.types.ObjectId
import replace.datastore.FloorRepository
import replace.datastore.LocationRepository
import replace.dto.FloorDto
import replace.dto.toDto
import replace.model.Floor

object CreateFloorUseCase {
    suspend fun execute(
        floorDto: FloorDto,
        floorRepository: FloorRepository,
        locationRepository: LocationRepository,
    ): FloorDto {
        val name = floorDto.name

        val locationId = ObjectId(floorDto.locationId)
        val location = locationRepository.findOneById(locationId)
        checkNotNull(location) { "Location ID not found" }
        val floor = Floor(name, locationId)

        val insertedFloor = floorRepository.insertOne(floor)
        checkNotNull(insertedFloor) { "Could not insert BookableEntity" }
        return insertedFloor.toDto()
    }
}
