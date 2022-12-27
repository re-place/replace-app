package replace.usecase.file

import org.bson.types.ObjectId
import replace.datastore.FileRepository
import replace.datastore.FileStorage

object DeleteFileUserCase {

    suspend fun execute(
        fileId: String,
        fileRepository: FileRepository,
        fileStorage: FileStorage
    ) {
        if (!ObjectId.isValid(fileId)) {
            throw IllegalArgumentException("Id $fileId: is not a valid ObjectId")
        }

        val file = fileRepository.findOneById(ObjectId(fileId))
            ?: throw IllegalArgumentException("File with id $fileId not found")

        if (fileStorage.deleteFile(file.path)) {
            fileRepository.deleteOneById(ObjectId(fileId))
        } else {
            throw IllegalStateException("Could not delete file with id $fileId")
        }
    }
}
