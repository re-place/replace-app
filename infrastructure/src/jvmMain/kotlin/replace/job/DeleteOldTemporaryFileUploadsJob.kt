package replace.job

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.minus
import kotlinx.datetime.toJavaInstant
import org.jetbrains.exposed.sql.SqlExpressionBuilder.less
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import replace.datastore.FileStorage
import replace.model.TemporaryFile
import replace.model.TemporaryFiles
import replace.usecase.temporaryfileupload.DeleteTemporaryFileUploadUseCase

class DeleteOldTemporaryFileUploadsJob(
    interval: Long,
    private val fileMaxAgeInMilliseconds: Long,
    private val fileStorage: FileStorage,
) : SchedulableJob(interval) {
    override suspend fun run() {
        val fileStorage = fileStorage
        try {
            newSuspendedTransaction {
                val oldTemporaryFiles = TemporaryFile.find(
                    TemporaryFiles.createdAt less Clock.System.now().minus(fileMaxAgeInMilliseconds, DateTimeUnit.MILLISECOND).toJavaInstant(),
                )

                oldTemporaryFiles.forEach { temporaryFile ->
                    DeleteTemporaryFileUploadUseCase.execute(temporaryFile, fileStorage)
                }
            }
        } catch (e: Exception) {
            println("Error while deleting old temporary file uploads: ${e.message}")
            e.printStackTrace()
        }
    }
}
