package replace.usecase.temporaryfileupload

import org.bson.types.ObjectId
import replace.datastore.Storage
import replace.datastore.TemporaryFileUploadRepository

object DeleteTemporaryFileUploadUseCase {

    suspend fun execute(
        temporaryFileUploadId: String,
        temporaryFileUploadRepository: TemporaryFileUploadRepository,
        storage: Storage
    ) {

        if (!ObjectId.isValid(temporaryFileUploadId)) {
            throw IllegalArgumentException("Id $temporaryFileUploadId: is not a valid ObjectId")
        }

        val temporaryFileUpload = temporaryFileUploadRepository.findOneById(ObjectId(temporaryFileUploadId))
            ?: throw IllegalArgumentException("Temporary file upload with id $temporaryFileUploadId not found")

        if (storage.deleteFile(temporaryFileUpload.path)) {
            temporaryFileUploadRepository.deleteOneById(ObjectId(temporaryFileUploadId))
        } else {
            throw IllegalStateException("Could not delete temporary file upload with id $temporaryFileUploadId")
        }
    }
}
