package replace.datastore

import kotlinx.coroutines.flow.Flow

interface Repository<T : Any> {
    suspend fun insertOne(item: T): T?
    suspend fun updateOne(item: T): T?
    suspend fun getOne(id: String): T?
    fun getAll(): Flow<T>
}
