package replace.datastore

import kotlinx.coroutines.flow.Flow
import replace.model.ObjectWithId

interface Repository<T : ObjectWithId> {
    suspend fun insertOne(item: T): T?
    suspend fun updateOne(item: T): T?
    suspend fun getOne(id: String): T?
    fun getAll(): Flow<T>
}
