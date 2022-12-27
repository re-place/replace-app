package replace.usecase.temporaryfileupload

import replace.datastore.FileStorage
import replace.datastore.TemporaryFileRepository
import replace.dto.TemporaryFileUploadDto
import replace.dto.toDto
import replace.model.TemporaryFile
import java.io.InputStream
import java.net.URLConnection

object CreateTemporaryFileUploadUseCase {

    suspend fun execute(
        fileName: String,
        input: InputStream,
        temporaryFileRepository: TemporaryFileRepository,
        fileStorage: FileStorage
    ): TemporaryFileUploadDto {

        val temporaryFileUploadPath = "temporary_uploads/${java.util.UUID.randomUUID()}"

        fileStorage.saveFile(temporaryFileUploadPath, input)

        val insertedTemporaryFile = temporaryFileRepository.insertOne(
            TemporaryFile(
                name = fileName.substringBeforeLast("."),
                path = temporaryFileUploadPath,
                mime = URLConnection.guessContentTypeFromName(fileName.lowercase()),
                extension = fileName.substringAfterLast("."),
                sizeInBytes = fileStorage.getFileSize(temporaryFileUploadPath),
                createdAt = java.time.LocalDateTime.now(),
            )
        )

        checkNotNull(insertedTemporaryFile) { "Could not insert TemporaryFileUpload into Database" }

        return insertedTemporaryFile.toDto()
    }
}
