package replace.http

import kotlinx.coroutines.flow.Flow
import org.bson.Document
import org.bson.types.ObjectId
import replace.datastore.Repository
import replace.model.ObjectWithId

class MockRepository<T : ObjectWithId>(val backing: MutableList<T>) : Repository<T> {
    override suspend fun insertOne(item: T): T {
        item.id = ObjectId()
        backing.add(item)
        return item
    }

    override suspend fun updateOne(item: T): T? {

    }

    override suspend fun getOne(id: String): T? {
        TODO("Not yet implemented")
    }

    override fun getAll(): Flow<T> {
        TODO("Not yet implemented")
    }
}
