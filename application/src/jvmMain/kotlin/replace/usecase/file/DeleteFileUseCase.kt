package replace.usecase.file

import replace.datastore.FileStorage
import replace.model.File

object DeleteFileUseCase {

    suspend fun execute(
        fileId: String,
        fileStorage: FileStorage,
    ) {

        val file = File.findById(fileId)

        checkNotNull(file) { "File with id $fileId not found" }

        if (fileStorage.deleteFile(file.path)) {
            file.delete()
        } else {
            throw IllegalStateException("Could not delete file with id $fileId")
        }
    }
}
