package replace.usecase.temporaryfileupload

import replace.datastore.FileRepository
import replace.datastore.Storage
import replace.datastore.TemporaryFileUploadRepository
import replace.dto.FileDto
import replace.usecase.file.CreateFileUseCase

object SaveTemporaryFileUploadPersistentUseCase {

    suspend fun execute(
        temporaryFileUploadId: String,
        temporaryFileUploadRepository: TemporaryFileUploadRepository,
        fileRepository: FileRepository,
        storage: Storage
    ): FileDto {

        val file = CreateFileUseCase.execute(
            temporaryFileUploadId,
            temporaryFileUploadRepository,
            fileRepository,
            storage,
        )

        DeleteTemporaryFileUploadUseCase.execute(
            temporaryFileUploadId,
            temporaryFileUploadRepository,
            storage,
        )

        return file
    }
}
