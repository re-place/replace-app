package replace.usecase.temporaryfileupload

import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import replace.datastore.FileStorage
import replace.dto.TemporaryFileUploadDto
import replace.dto.toDto
import replace.model.TemporaryFile
import java.io.InputStream
import java.net.URLConnection
import java.util.UUID.randomUUID

object CreateTemporaryFileUploadUseCase {

    suspend fun execute(
        fileName: String,
        input: InputStream,
        fileStorage: FileStorage,
    ): TemporaryFileUploadDto {
        return newSuspendedTransaction {
            val temporaryFileUploadPath = "temporary_uploads/${randomUUID()}"

            fileStorage.saveFile(temporaryFileUploadPath, input)

            val fileSize = fileStorage.getFileSize(temporaryFileUploadPath)

            val temporaryFile = TemporaryFile.new {
                name = fileName.substringBeforeLast(".")
                path = temporaryFileUploadPath
                mime = URLConnection.guessContentTypeFromName(fileName.lowercase())
                extension = fileName.substringAfterLast(".")
                sizeInBytes = fileSize
                createdAt = Clock.System.now()
            }

            temporaryFile.toDto()
        }
    }
}
