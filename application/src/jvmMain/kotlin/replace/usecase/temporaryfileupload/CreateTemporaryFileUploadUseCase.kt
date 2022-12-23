package replace.usecase.temporaryfileupload

import replace.datastore.Storage
import replace.datastore.TemporaryFileUploadRepository
import replace.dto.TemporaryFileUploadDto
import replace.dto.toDto
import replace.model.TemporaryFileUpload
import java.io.InputStream
import java.net.URLConnection

object CreateTemporaryFileUploadUseCase {

    suspend fun execute(
        fileName: String,
        input: InputStream,
        temporaryFileUploadRepository: TemporaryFileUploadRepository,
        storage: Storage
    ): TemporaryFileUploadDto {

        val temporaryFileUploadPath = "temporary_uploads/${java.util.UUID.randomUUID()}"

        val temporaryFileUpload = storage.saveFile(temporaryFileUploadPath, input)

        val insertedTemporaryFileUpload = temporaryFileUploadRepository.insertOne(
            TemporaryFileUpload(
                name = fileName.substringBeforeLast("."),
                path = temporaryFileUploadPath,
                mime = URLConnection.guessContentTypeFromName(fileName.lowercase()),
                extension = fileName.substringAfterLast("."),
                sizeInBytes = temporaryFileUpload.length().toInt(),
                createdAt = java.time.LocalDateTime.now(),
            )
        )

        checkNotNull(insertedTemporaryFileUpload) { "Could not insert TemporaryFileUpload into Database" }

        return insertedTemporaryFileUpload.toDto()
    }
}
