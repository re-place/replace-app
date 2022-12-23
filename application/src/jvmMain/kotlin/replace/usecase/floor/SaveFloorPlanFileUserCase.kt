package replace.usecase.floor

import org.bson.types.ObjectId
import replace.datastore.FileRepository
import replace.datastore.FloorRepository
import replace.datastore.Storage
import replace.datastore.TemporaryFileUploadRepository
import replace.dto.FileUploadDto
import replace.dto.FloorDto
import replace.usecase.file.DeleteFileUserCase
import replace.usecase.temporaryfileupload.SaveTemporaryFileUploadPersistentUseCase

object SaveFloorPlanFileUserCase {
    suspend fun execute(
        floorDto: FloorDto,
        floorRepository: FloorRepository,
        temporaryFileUploadRepository: TemporaryFileUploadRepository,
        fileRepository: FileRepository,
        storage: Storage,
    ): FloorDto {
        val currentPlanFileId = floorDto.id?.let { floorRepository.findOneById(ObjectId(it)) }?.planFileId

        if (currentPlanFileId != null) {
            DeleteFileUserCase.execute(
                currentPlanFileId.toHexString(),
                fileRepository,
                storage,
            )
        }

        if (floorDto.planFile == null) {
            return floorDto
        }

        if (!floorDto.planFile.temporary) {
            return floorDto
        }

        val file = SaveTemporaryFileUploadPersistentUseCase.execute(
            floorDto.planFile.id,
            temporaryFileUploadRepository,
            fileRepository,
            storage,
        )

        checkNotNull(file.id) { "Could not save file" }

        return FloorDto(
            id = floorDto.id,
            name = floorDto.name,
            siteId = floorDto.siteId,
            planFile = FileUploadDto(
                id = file.id,
                temporary = false,
            ),
        )
    }
}
