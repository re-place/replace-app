package replace.datastore

import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq
import replace.model.User

class MongoUserRepository(collection: CoroutineCollection<User>) :
    MongoRepository<User>(collection), UserRepository {
    override suspend fun findByUsername(username: String): User? =
        collection.findOne(User::username eq username)
}
