package replace.usecase.temporaryfileupload

import replace.datastore.FileRepository
import replace.datastore.FileStorage
import replace.datastore.TemporaryFileRepository
import replace.dto.FileDto
import replace.usecase.file.CreateFileUseCase

object SaveTemporaryFileUploadPersistentUseCase {

    suspend fun execute(
        temporaryFileUploadId: String,
        temporaryFileRepository: TemporaryFileRepository,
        fileRepository: FileRepository,
        fileStorage: FileStorage
    ): FileDto {

        val file = CreateFileUseCase.execute(
            temporaryFileUploadId,
            temporaryFileRepository,
            fileRepository,
            fileStorage,
        )

        DeleteTemporaryFileUploadUseCase.execute(
            temporaryFileUploadId,
            temporaryFileRepository,
            fileStorage,
        )

        return file
    }
}
