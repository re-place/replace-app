package replace.datastore

import replace.model.Users

interface UserRepository : Repository<Users> {
    suspend fun findByUsername(username: String): Users?
}
