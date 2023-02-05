package replace.model

import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toKotlinInstant
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant

object Bookings : Models("bookings") {
    val start = timestamp("start").default(Instant.now())
    val end = timestamp("end").default(Instant.now())
    val user_id = reference("user_id", Users)
}

class Booking(id: EntityID<String>) : Model(id) {
    companion object : EntityClass<String, Booking>(Bookings)
    var start by Bookings.start.transform({ it.toJavaInstant() }, { it.toKotlinInstant() })
    var end by Bookings.end.transform({ it.toJavaInstant() }, { it.toKotlinInstant() })

    var userId by Bookings.user_id
    var user by User referencedOn Bookings.user_id

    var bookedEntities by BookableEntity via BookedEntities
}
