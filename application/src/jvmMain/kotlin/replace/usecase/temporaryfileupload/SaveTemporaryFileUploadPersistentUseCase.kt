package replace.usecase.temporaryfileupload

import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import replace.datastore.FileStorage
import replace.dto.FileDto
import replace.usecase.file.CreateFileUseCase

object SaveTemporaryFileUploadPersistentUseCase {

    suspend fun execute(
        temporaryFileUploadId: String,
        fileStorage: FileStorage,
    ): FileDto {
        return newSuspendedTransaction {
            val file = CreateFileUseCase.execute(
                temporaryFileUploadId,
                fileStorage,
            )

            DeleteTemporaryFileUploadUseCase.execute(
                temporaryFileUploadId,
                fileStorage,
            )

            file
        }
    }
}
