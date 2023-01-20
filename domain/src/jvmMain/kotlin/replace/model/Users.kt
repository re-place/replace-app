package replace.model

import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

object Users : Models() {
    val username = varchar("username", 255).uniqueIndex()
    val password = varchar("password", 255)
    val firstname = varchar("firstname", 255)
    val lastname = varchar("lastname", 255)
}

class User(id: EntityID<String>) : Model(id) {
    companion object : EntityClass<String, User>(Users)
    var username by Users.username
    var password by Users.password
    var firstname by Users.firstname
    var lastname by Users.lastname

    val bookings by Booking referrersOn Bookings.user_id
}
