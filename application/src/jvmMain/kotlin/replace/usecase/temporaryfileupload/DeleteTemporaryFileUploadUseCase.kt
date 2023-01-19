package replace.usecase.temporaryfileupload

import org.bson.types.ObjectId
import replace.datastore.FileStorage
import replace.datastore.TemporaryFileRepository

object DeleteTemporaryFileUploadUseCase {

    suspend fun execute(
        temporaryFileUploadId: String,
        temporaryFileRepository: TemporaryFileRepository,
        fileStorage: FileStorage,
    ) {

        if (!ObjectId.isValid(temporaryFileUploadId)) {
            throw IllegalArgumentException("Id $temporaryFileUploadId is not a valid ObjectId")
        }

        val temporaryFileUpload = temporaryFileRepository.findOneById(ObjectId(temporaryFileUploadId))
            ?: throw IllegalArgumentException("Temporary file upload with id $temporaryFileUploadId not found")

        if (fileStorage.deleteFile(temporaryFileUpload.path)) {
            temporaryFileRepository.deleteOneById(ObjectId(temporaryFileUploadId))
        } else {
            throw IllegalStateException("Could not delete temporary file upload with id $temporaryFileUploadId")
        }
    }
}
