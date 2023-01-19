package replace.usecase.temporaryfileupload

import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import replace.datastore.FileStorage
import replace.model.TemporaryFile

object DeleteTemporaryFileUploadUseCase {

    suspend fun execute(
        temporaryFileId: String,
        fileStorage: FileStorage,
    ) {
        newSuspendedTransaction {
            val temporaryFile = TemporaryFile.findById(temporaryFileId)

            checkNotNull(temporaryFile) { "TemporaryFileUpload with id $temporaryFileId not found" }

            if (fileStorage.deleteFile(temporaryFile.path)) {
                temporaryFile.delete()
            } else {
                throw IllegalStateException("Could not delete temporary file upload with id $temporaryFileId")
            }
        }
    }

    suspend fun execute(
        temporaryFile: TemporaryFile,
        fileStorage: FileStorage,
    ) {
        if (fileStorage.deleteFile(temporaryFile.path)) {
            temporaryFile.delete()
        } else {
            throw IllegalStateException("Could not delete temporary file upload with id ${temporaryFile.id}")
        }
    }
}
