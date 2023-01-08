package replace.datastore

import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.lt
import replace.model.TemporaryFile
import java.time.LocalDateTime

class MongoTemporaryFileRepository(collection: CoroutineCollection<TemporaryFile>) :
    MongoRepository<TemporaryFile>(collection), TemporaryFileRepository {

    override suspend fun findOlderThan(datetime: LocalDateTime): List<TemporaryFile> =
        collection.find(TemporaryFile::createdAt lt datetime).toList()
}
