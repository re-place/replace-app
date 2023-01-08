package replace.usecase.floor

import org.bson.types.ObjectId
import replace.datastore.FileRepository
import replace.datastore.FileStorage
import replace.datastore.FloorRepository
import replace.datastore.TemporaryFileRepository
import replace.dto.FloorDto
import replace.dto.saveFiles
import replace.usecase.file.DeleteFileUseCase

object SaveFloorPlanFileUseCase {
    suspend fun execute(
        floorDto: FloorDto,
        floorRepository: FloorRepository,
        temporaryFileRepository: TemporaryFileRepository,
        fileRepository: FileRepository,
        fileStorage: FileStorage,
    ): FloorDto {
        val oldPlanFileId = floorDto.id?.let { floorRepository.findOneById(ObjectId(it)) }?.planFileId?.toHexString()

        val saved = floorDto.saveFiles(
            temporaryFileRepository = temporaryFileRepository,
            fileRepository = fileRepository,
            fileStorage = fileStorage,
        )

        if (oldPlanFileId != saved.planFile?.id) {
            oldPlanFileId?.let { DeleteFileUseCase.execute(it, fileRepository, fileStorage) }
        }

        return saved
    }
}
