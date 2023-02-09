package replace.model

import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

object Users : Models() {
    val email = varchar("email", 255).uniqueIndex()
    val firstname = varchar("firstname", 255)
    val lastname = varchar("lastname", 255)
}

class User(id: EntityID<String>) : Model(id) {
    companion object : EntityClass<String, User>(Users)
    var email by Users.email
    var firstname by Users.firstname
    var lastname by Users.lastname

    val bookings by Booking referrersOn Bookings.user_id
}
