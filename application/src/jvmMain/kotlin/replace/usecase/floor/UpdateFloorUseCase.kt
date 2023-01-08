package replace.usecase.floor

import org.bson.types.ObjectId
import replace.datastore.FileRepository
import replace.datastore.FileStorage
import replace.datastore.FloorRepository
import replace.datastore.TemporaryFileRepository
import replace.dto.FloorDto
import replace.dto.toDto
import replace.dto.toModel

object UpdateFloorUseCase {
    suspend fun execute(
        dto: FloorDto,
        repository: FloorRepository,
        temporaryFileRepository: TemporaryFileRepository,
        fileRepository: FileRepository,
        fileStorage: FileStorage,
    ): FloorDto {
        val floorId = ObjectId(dto.id)

        val floorDtoWithPlan = SaveFloorPlanFileUseCase.execute(
            dto,
            repository,
            temporaryFileRepository,
            fileRepository,
            fileStorage,
        )

        val updatedModel = repository.updateOne(floorId, floorDtoWithPlan.toModel())

        checkNotNull(updatedModel) { "Could not update Floor with id $floorId\n$floorDtoWithPlan" }

        return updatedModel.toDto()
    }
}
