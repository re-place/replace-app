package replace.model

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.javatime.datetime

object Bookings : Model() {
    val start = datetime("start")
    val end = datetime("end")
    val user_id = reference("user_id", Users)
}

class Booking(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, Booking>(Bookings)
    var start by Bookings.start
    var end by Bookings.end

    var userId by Bookings.user_id
    var user by User referencedOn Bookings.user_id

    val bookedEntities by BookableEntity via BookedEntities
}
