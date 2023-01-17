package replace.model

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

object Users: Model() {
    val username = varchar("username", 255).uniqueIndex()
    val password = varchar("password", 255)
    val firstName = varchar("firstName", 255)
    val lastName = varchar("lastName", 255)
}

class User(id: EntityID<String>) : Entity<String>(id) {
    companion object: EntityClass<String, User>(Users)
    var username by Users.username
    var password by Users.password
    var firstName by Users.firstName
    var lastName by Users.lastName

    val bookings by Booking referrersOn Bookings.user_id
}
