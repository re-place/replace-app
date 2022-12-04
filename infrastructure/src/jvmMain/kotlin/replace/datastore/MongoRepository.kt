package replace.datastore

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.litote.kmongo.coroutine.CoroutineCollection

class MongoRepository<T : Any>(private val collection: CoroutineCollection<T>) : Repository<T> {
    override suspend fun insertOne(item: T): T? {
        return if (collection.insertOne(item).wasAcknowledged()) {
            return item
        } else null
    }

    override suspend fun updateOne(item: T): T? {
        TODO("Not yet implemented")
    }

    override suspend fun getOne(id: String): T? {
        TODO("Not yet implemented")
    }

    override fun getAll(): Flow<T> {
        return emptyFlow()
    }
}
