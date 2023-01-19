package replace.usecase.file

import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import replace.datastore.FileStorage
import replace.dto.FileDto
import replace.dto.toDto
import replace.model.File
import replace.model.TemporaryFile
import java.util.UUID

object CreateFileUseCase {

    suspend fun execute(
        temporaryFileUploadId: String,
        fileStorage: FileStorage,
    ): FileDto {
        return newSuspendedTransaction {

            val temporaryFileUpload = TemporaryFile.findById(temporaryFileUploadId)
                ?: throw IllegalArgumentException("Temporary file upload with id $temporaryFileUploadId not found")

            val newFilePath = "uploads/${UUID.randomUUID()}/base.${temporaryFileUpload.extension}"

            if (!fileStorage.copyFile(temporaryFileUpload.path, newFilePath)) {
                throw IllegalStateException("Could not copy file from ${temporaryFileUpload.path} to $newFilePath")
            }

            val fileSize = fileStorage.getFileSize(newFilePath)

            val insertedFile = File.new {
                name = temporaryFileUpload.name
                path = newFilePath
                mime = temporaryFileUpload.mime
                extension = temporaryFileUpload.extension
                sizeInBytes = fileSize
            }

            insertedFile.toDto()
        }
    }
}
