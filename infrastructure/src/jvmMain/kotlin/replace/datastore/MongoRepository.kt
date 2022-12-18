package replace.datastore

import org.bson.types.ObjectId
import org.litote.kmongo.coroutine.CoroutineCollection
import replace.model.ObjectWithId

open class MongoRepository<T : ObjectWithId>(protected val collection: CoroutineCollection<T>) : Repository<T> {
    override suspend fun insertOne(item: T): T? =
        if (collection.insertOne(item).wasAcknowledged()) item else null

    override suspend fun updateOne(id: ObjectId, item: T): T? =
        if (collection.updateOneById(id, item).wasAcknowledged()) item else null

    override suspend fun findOneById(id: ObjectId): T? =
        collection.findOneById(id)

    override suspend fun deleteOneById(id: ObjectId): Boolean =
        collection.deleteOneById(id).deletedCount > 0

    override suspend fun getAll(): List<T> =
        collection.find().toList()
}
