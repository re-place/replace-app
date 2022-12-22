package replace.usecase.file

import org.bson.types.ObjectId
import replace.datastore.FileRepository
import replace.datastore.Storage
import replace.datastore.TemporaryFileUploadRepository
import replace.dto.FileDto
import replace.dto.toDto
import replace.model.File
import java.util.UUID

object CreateFileUseCase {

    suspend fun execute(
        temporaryFileUploadId: String,
        temporaryFileUploadRepository: TemporaryFileUploadRepository,
        fileRepository: FileRepository,
        storage: Storage
    ): FileDto {
        if (!ObjectId.isValid(temporaryFileUploadId)) {
            throw IllegalArgumentException("Id $temporaryFileUploadId is not a valid ObjectId")
        }

        val temporaryFileUpload = temporaryFileUploadRepository.findOneById(ObjectId(temporaryFileUploadId))
            ?: throw IllegalArgumentException("Temporary file upload with id $temporaryFileUploadId not found")

        val newFilePath = "uploads/${UUID.randomUUID()}/base.${temporaryFileUpload.extension}"

        val newDiskFile = storage.copyFile(temporaryFileUpload.path, newFilePath)

        val file = File(
            name = temporaryFileUpload.name,
            path = newFilePath,
            mime = temporaryFileUpload.mime,
            extension = temporaryFileUpload.extension,
            sizeInBytes = newDiskFile.length().toInt(),
        )

        val insertedFile = fileRepository.insertOne(file)

        checkNotNull(insertedFile) { "Could not insert File into Database" }

        return insertedFile.toDto()
    }
}
