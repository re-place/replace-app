package replace.job

import org.jetbrains.exposed.sql.SqlExpressionBuilder.less
import org.jetbrains.exposed.sql.transactions.transaction
import replace.datastore.FileStorage
import replace.model.TemporaryFile
import replace.model.TemporaryFiles
import replace.usecase.temporaryfileupload.DeleteTemporaryFileUploadUseCase
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class DeleteOldTemporaryFileUploadsJob(
    interval: Long,
    private val fileMaxAgeInMilliseconds: Long,
    private val fileStorage: FileStorage,
) : SchedulableJob(interval) {
    override suspend fun run() {

        try {
            val oldTemporaryFiles = transaction {
                TemporaryFile.find(TemporaryFiles.createdAt less LocalDateTime.now().minus(fileMaxAgeInMilliseconds, ChronoUnit.MILLIS))
            }
            println("Found ${oldTemporaryFiles.count()} old temporary files")
            oldTemporaryFiles.forEach { temporaryFile ->
                DeleteTemporaryFileUploadUseCase.execute(temporaryFile, this.fileStorage)
            }
        } catch (e: Exception) {
            println("Error while deleting old temporary file uploads: ${e.message}")
            e.printStackTrace()
        }
    }
}
