package replace.datastore

import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.lt
import replace.model.TemporaryFileUpload
import java.time.LocalDateTime

class MongoTemporaryFileUploadRepository(collection: CoroutineCollection<TemporaryFileUpload>) :
    MongoRepository<TemporaryFileUpload>(collection), TemporaryFileUploadRepository {

    override suspend fun findOlderThan(datetime: LocalDateTime): List<TemporaryFileUpload> =
        collection.find(TemporaryFileUpload::createdAt lt datetime).toList()
}
