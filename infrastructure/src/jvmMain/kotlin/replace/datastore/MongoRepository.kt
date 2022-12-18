package replace.datastore

import org.bson.types.ObjectId
import org.litote.kmongo.coroutine.CoroutineCollection
import replace.model.ObjectWithMaybeId

open class MongoRepository<T : ObjectWithMaybeId>(protected val collection: CoroutineCollection<T>) : Repository<T> {
    override suspend fun insertOne(item: T): T? =
        if (collection.insertOne(item).wasAcknowledged()) item else null

    // TODO: Should not replace but merge document with existing in DB
    override suspend fun updateOne(item: T): T? =
        item.id?.let { id -> if (collection.replaceOneById(id, item).wasAcknowledged().modifiedCount > 0) item else null }

    override suspend fun findOneById(id: ObjectId): T? =
        collection.findOneById(id)

    override suspend fun deleteOneById(id: ObjectId): Boolean =
        collection.deleteOneById(id).deletedCount > 0

    override suspend fun getAll(): List<T> =
        collection.find().toList()
}
