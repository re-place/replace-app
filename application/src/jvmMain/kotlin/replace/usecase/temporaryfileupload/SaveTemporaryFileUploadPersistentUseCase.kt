package replace.usecase.temporaryfileupload

import replace.datastore.FileStorage
import replace.dto.FileDto
import replace.usecase.file.CreateFileUseCase

object SaveTemporaryFileUploadPersistentUseCase {

    suspend fun execute(
        temporaryFileUploadId: String,
        fileStorage: FileStorage,
    ): FileDto {

        val file = CreateFileUseCase.execute(
            temporaryFileUploadId,
            fileStorage,
        )

        DeleteTemporaryFileUploadUseCase.execute(
            temporaryFileUploadId,
            fileStorage,
        )

        return file
    }
}
