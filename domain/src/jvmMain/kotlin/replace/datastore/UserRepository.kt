package replace.datastore

import replace.model.User

interface UserRepository : Repository<User> {
    suspend fun findByUsername(username: String): User?
}
