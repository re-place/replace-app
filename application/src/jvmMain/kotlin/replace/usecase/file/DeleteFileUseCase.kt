package replace.usecase.file

import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import replace.datastore.FileStorage
import replace.model.File

object DeleteFileUseCase {

    suspend fun execute(
        fileId: String,
        fileStorage: FileStorage,
    ) {
        return newSuspendedTransaction {
            val file = File.findById(fileId)

            checkNotNull(file) { "File with id $fileId not found" }

            if (fileStorage.deleteFile(file.path)) {
                file.delete()
            } else {
                throw IllegalStateException("Could not delete file with id $fileId")
            }
        }
    }
}
