package replace.datastore

import org.bson.types.ObjectId
import replace.model.Model

interface Repository<T : Model> {
    suspend fun insertOne(item: T): T?
    suspend fun updateOne(id: ObjectId, item: T): T?
    suspend fun findOneById(id: ObjectId): T?
    suspend fun deleteOneById(id: ObjectId): Boolean
    suspend fun getAll(): List<T>
}
