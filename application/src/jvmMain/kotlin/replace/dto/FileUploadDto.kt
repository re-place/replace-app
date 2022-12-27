package replace.dto

import kotlinx.serialization.Serializable
import replace.datastore.FileRepository
import replace.datastore.FileStorage
import replace.datastore.TemporaryFileRepository
import replace.usecase.temporaryfileupload.SaveTemporaryFileUploadPersistentUseCase

@Serializable
class FileUploadDto(
    override val id: String,
    val temporary: Boolean,
) : Dto

suspend fun FileUploadDto.save(
    temporaryFileRepository: TemporaryFileRepository,
    fileRepository: FileRepository,
    fileStorage: FileStorage,
): FileUploadDto {
    if (!temporary) {
        return this
    }

    val file = SaveTemporaryFileUploadPersistentUseCase.execute(
        id,
        temporaryFileRepository,
        fileRepository,
        fileStorage,
    )

    checkNotNull(file.id) { "Could not save file" }

    return FileUploadDto(
        id = file.id,
        temporary = false,
    )
}
