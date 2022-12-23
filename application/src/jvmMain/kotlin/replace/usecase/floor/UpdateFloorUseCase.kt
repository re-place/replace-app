package replace.usecase.floor

import org.bson.types.ObjectId
import replace.datastore.FileRepository
import replace.datastore.FloorRepository
import replace.datastore.Storage
import replace.datastore.TemporaryFileUploadRepository
import replace.dto.FloorDto
import replace.dto.toDto
import replace.dto.toModel

object UpdateFloorUseCase {
    suspend fun execute(
        dto: FloorDto,
        repository: FloorRepository,
        temporaryFileUploadRepository: TemporaryFileUploadRepository,
        fileRepository: FileRepository,
        storage: Storage,
    ): FloorDto {
        val floorId = ObjectId(dto.id)

        val floorDtoWithPlan = SaveFloorPlanFileUserCase.execute(
            dto,
            repository,
            temporaryFileUploadRepository,
            fileRepository,
            storage,
        )

        val updatedModel = repository.updateOne(floorId, floorDtoWithPlan.toModel())

        checkNotNull(updatedModel) { "Could not update Floor" }

        return updatedModel.toDto()
    }
}
