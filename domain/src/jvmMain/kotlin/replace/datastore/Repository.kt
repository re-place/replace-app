package replace.datastore

import org.bson.types.ObjectId
import replace.model.ObjectWithId

interface Repository<T : ObjectWithId> {
    suspend fun insertOne(item: T): T?
    suspend fun updateOne(item: T): T?
    suspend fun findOneById(id: ObjectId): T?
    suspend fun deleteOneById(id: ObjectId): Boolean
    suspend fun getAll(): List<T>
}
