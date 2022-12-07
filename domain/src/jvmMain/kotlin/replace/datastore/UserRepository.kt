package replace.datastore

import replace.model.User

interface UserRepository : Repository<User> {
    suspend fun findByUserName(userName: String): User?
}
