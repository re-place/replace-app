package replace.job

import replace.datastore.FileStorage
import replace.datastore.TemporaryFileRepository
import replace.usecase.temporaryfileupload.DeleteTemporaryFileUploadUseCase
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class DeleteOldTemporaryFileUploadsJob(
    interval: Long,
    private val fileMaxAgeInMilliseconds: Long,
    private val temporaryFileRepository: TemporaryFileRepository,
    private val fileStorage: FileStorage,
) : SchedulableJob(interval) {
    override suspend fun run() {

        try {
            val oldTemporaryFileUploads = this.temporaryFileRepository.findOlderThan(LocalDateTime.now().minus(fileMaxAgeInMilliseconds, ChronoUnit.MILLIS))

            oldTemporaryFileUploads.forEach { temporaryFileUpload ->
                temporaryFileUpload.id?.let {
                    DeleteTemporaryFileUploadUseCase.execute(it.toHexString(), this.temporaryFileRepository, this.fileStorage)
                }
            }
        } catch (e: Exception) {
            println("Error while deleting old temporary file uploads: ${e.message}")
            e.printStackTrace()
        }
    }
}
