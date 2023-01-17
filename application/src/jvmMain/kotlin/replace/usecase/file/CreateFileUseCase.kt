package replace.usecase.file

import org.bson.types.ObjectId
import replace.datastore.FileRepository
import replace.datastore.FileStorage
import replace.datastore.TemporaryFileRepository
import replace.dto.FileDto
import replace.dto.toDto
import java.util.UUID

object CreateFileUseCase {

    suspend fun execute(
        temporaryFileUploadId: String,
        temporaryFileRepository: TemporaryFileRepository,
        fileRepository: FileRepository,
        fileStorage: FileStorage,
    ): FileDto {
        if (!ObjectId.isValid(temporaryFileUploadId)) {
            throw IllegalArgumentException("Id $temporaryFileUploadId is not a valid ObjectId")
        }

        val temporaryFileUpload = temporaryFileRepository.findOneById(ObjectId(temporaryFileUploadId))
            ?: throw IllegalArgumentException("Temporary file upload with id $temporaryFileUploadId not found")

        val newFilePath = "uploads/${UUID.randomUUID()}/base.${temporaryFileUpload.extension}"

        if (!fileStorage.copyFile(temporaryFileUpload.path, newFilePath)) {
            throw IllegalStateException("Could not copy file from ${temporaryFileUpload.path} to $newFilePath")
        }

        val file = File(
            name = temporaryFileUpload.name,
            path = newFilePath,
            mime = temporaryFileUpload.mime,
            extension = temporaryFileUpload.extension,
            sizeInBytes = fileStorage.getFileSize(newFilePath),
        )

        val insertedFile = fileRepository.insertOne(file)

        checkNotNull(insertedFile) { "Could not insert File into Database" }

        return insertedFile.toDto()
    }
}
