package replace.usecase.floor

import org.bson.types.ObjectId
import replace.datastore.FloorRepository
import replace.dto.FloorDto
import replace.dto.toDto
import replace.dto.toModel

object UpdateFloorUseCase {
    suspend fun execute(
        dto: FloorDto,
        repository: FloorRepository,
    ): FloorDto {
        val floorId = ObjectId(dto.id)

        val updatedModel = repository.updateOne(floorId, dto.toModel())

        checkNotNull(updatedModel) { "Could not update Floor" }

        return updatedModel.toDto()
    }
}
