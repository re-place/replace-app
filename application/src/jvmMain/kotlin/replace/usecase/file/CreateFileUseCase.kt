package replace.usecase.file

import org.bson.types.ObjectId
import replace.datastore.FileStorage
import replace.dto.FileDto
import replace.dto.toDto
import java.util.UUID
import replace.model.File
import replace.model.TemporaryFile

object CreateFileUseCase {

    suspend fun execute(
        temporaryFileUploadId: String,
        fileStorage: FileStorage,
    ): FileDto {
        if (!ObjectId.isValid(temporaryFileUploadId)) {
            throw IllegalArgumentException("Id $temporaryFileUploadId is not a valid ObjectId")
        }

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

        return insertedFile.toDto()
    }
}
