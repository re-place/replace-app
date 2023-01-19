package replace.usecase.temporaryfileupload

import replace.datastore.FileStorage
import replace.dto.TemporaryFileUploadDto
import replace.dto.toDto
import java.io.InputStream
import java.net.URLConnection
import java.util.UUID.randomUUID
import replace.model.TemporaryFile

object CreateTemporaryFileUploadUseCase {

    suspend fun execute(
        fileName: String,
        input: InputStream,
        fileStorage: FileStorage,
    ): TemporaryFileUploadDto {

        val temporaryFileUploadPath = "temporary_uploads/${randomUUID()}"

        fileStorage.saveFile(temporaryFileUploadPath, input)

        val fileSize = fileStorage.getFileSize(temporaryFileUploadPath)

        val temporaryFile = TemporaryFile.new {
            name = fileName.substringBeforeLast(".")
            path = temporaryFileUploadPath
            mime = URLConnection.guessContentTypeFromName(fileName.lowercase())
            extension = fileName.substringAfterLast(".")
            sizeInBytes = fileSize
            createdAt = java.time.LocalDateTime.now()
        }

        return temporaryFile.toDto()
    }
}
