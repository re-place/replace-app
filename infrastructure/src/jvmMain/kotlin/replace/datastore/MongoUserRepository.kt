package replace.datastore

import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq
import replace.model.Users

class MongoUserRepository(collection: CoroutineCollection<Users>) :
    MongoRepository<Users>(collection), UserRepository {
    override suspend fun findByUsername(username: String): Users? =
        collection.findOne(Users::username eq username)
}
