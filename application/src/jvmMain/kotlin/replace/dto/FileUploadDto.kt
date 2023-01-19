package replace.dto

import kotlinx.serialization.Serializable
import replace.datastore.FileStorage
import replace.model.File
import replace.model.TemporaryFile
import replace.usecase.temporaryfileupload.SaveTemporaryFileUploadPersistentUseCase

@Serializable
data class FileUploadDto(
    val fileId: String,
    val temporary: Boolean,
)

suspend fun FileUploadDto.save(
    fileStorage: FileStorage,
): FileUploadDto {
    if (!temporary) {
        return this
    }

    val file = SaveTemporaryFileUploadPersistentUseCase.execute(
        fileId,
        fileStorage,
    )

    checkNotNull(file.id) { "Could not save file" }

    return FileUploadDto(
        fileId = file.id,
        temporary = false,
    )
}
