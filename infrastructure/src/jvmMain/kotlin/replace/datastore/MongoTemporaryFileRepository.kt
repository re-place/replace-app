package replace.datastore

import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.lt
import replace.model.TemporaryFiles
import java.time.LocalDateTime

class MongoTemporaryFileRepository(collection: CoroutineCollection<TemporaryFiles>) :
    MongoRepository<TemporaryFiles>(collection), TemporaryFileRepository {

    override suspend fun findOlderThan(datetime: LocalDateTime): List<TemporaryFiles> =
        collection.find(TemporaryFiles::createdAt lt datetime).toList()
}
