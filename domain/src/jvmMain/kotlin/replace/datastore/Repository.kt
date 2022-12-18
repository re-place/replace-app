package replace.datastore

import org.bson.types.ObjectId
import replace.model.ObjectWithId
import replace.model.ObjectWithMaybeId

@Suppress("BOUNDS_NOT_ALLOWED_IF_BOUNDED_BY_TYPE_PARAMETER")
interface Repository<T, R> where T : ObjectWithMaybeId, R : T, R : ObjectWithId {
    suspend fun insertOne(item: T): R?
    suspend fun updateOne(item: R): R?
    suspend fun findOneById(id: ObjectId): R?
    suspend fun deleteOneById(id: ObjectId): Boolean
    suspend fun getAll(): List<R>
}
