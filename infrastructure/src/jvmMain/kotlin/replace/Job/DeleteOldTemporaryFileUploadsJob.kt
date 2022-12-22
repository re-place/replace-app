package replace.Job

import replace.datastore.Storage
import replace.datastore.TemporaryFileUploadRepository
import replace.usecase.temporaryfileupload.DeleteTemporaryFileUploadUseCase
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class DeleteOldTemporaryFileUploadsJob(
    interval: Long,
    private val fileMaxAgeInMilliseconds: Long,
    private val temporaryFileUploadRepository: TemporaryFileUploadRepository,
    private val storage: Storage,
) : Job(interval) {
    override suspend fun run() {

        try {
            val oldTemporaryFileUploads = this.temporaryFileUploadRepository.findOlderThan(LocalDateTime.now().minus(fileMaxAgeInMilliseconds, ChronoUnit.MILLIS))

            oldTemporaryFileUploads.forEach { temporaryFileUpload ->
                temporaryFileUpload.id?.let {
                    DeleteTemporaryFileUploadUseCase.execute(it.toHexString(), this.temporaryFileUploadRepository, this.storage)
                }
            }
        } catch (e: Exception) {
            println("Error while deleting old temporary file uploads: ${e.message}\n${e.stackTrace.joinToString("\n")}")
        }
    }
}
