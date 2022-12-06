package replace.datastore

import kotlinx.coroutines.flow.Flow
import org.litote.kmongo.coroutine.CoroutineCollection
import replace.model.ObjectWithId

class MongoRepository<T : ObjectWithId>(private val collection: CoroutineCollection<T>) : Repository<T> {
    override suspend fun insertOne(item: T): T? =
        if (collection.insertOne(item).wasAcknowledged()) item else null

    // TODO: Should not replace but merge document with existing in DB
    override suspend fun updateOne(item: T): T? =
        item._id?.let { id -> if (collection.replaceOneById(id, item).modifiedCount > 0) item else null }

    override suspend fun getOne(id: String): T? =
        collection.findOneById(id)

    override suspend fun deleteOne(id: String): Boolean =
        collection.deleteOneById(id).deletedCount > 0

    override suspend fun getAll(): List<T> {
        println("getAll 1")
        val result = collection.find().toList()
        println("getAll: $result")
        return result
    }
}
