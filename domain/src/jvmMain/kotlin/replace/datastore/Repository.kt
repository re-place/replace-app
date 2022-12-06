package replace.datastore

import replace.model.ObjectWithId

interface Repository<T : ObjectWithId> {
    suspend fun insertOne(item: T): T?
    suspend fun updateOne(item: T): T?
    suspend fun getOne(id: String): T?
    suspend fun deleteOne(id: String): Boolean
    suspend fun getAll(): List<T>
}
