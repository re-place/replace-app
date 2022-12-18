package replace.usecase.datastore

import org.bson.types.ObjectId
import replace.datastore.Repository
import replace.model.ObjectWithId
import replace.model.ObjectWithMaybeId

@Suppress("BOUNDS_NOT_ALLOWED_IF_BOUNDED_BY_TYPE_PARAMETER")
class MockRepository<T, R> : Repository<T, R> where T : ObjectWithMaybeId, R : T, R : ObjectWithId {

    private val backing = mutableMapOf<ObjectId, T>()

    override suspend fun insertOne(item: T): R {
        val toInsert = item.takeIf { it.id != null} ?: item.copy(id = ObjectId()) }
        backing[id] = item
        return item
    }

    override suspend fun updateOne(item: R): R? {
        val id = item.id
        backing[id] = item
        return item
    }

    override suspend fun findOneById(id: ObjectId): T? = backing[id]
    override suspend fun deleteOneById(id: ObjectId): Boolean = backing.remove(id) != null
    override suspend fun getAll(): List<T> = backing.values.toList()
}
