package replace.model

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.javatime.datetime

object BookedEntities : Model() {
    val bookable_entity_id = reference("bookable_entity_id", BookableEntities)
    val booking_id = reference("booking_id", Bookings)
}

class BookedEntity(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, BookedEntity>(BookedEntities)

    var bookableEntityId by BookedEntities.bookable_entity_id
    var bookableEntity by BookableEntity referencedOn BookedEntities.bookable_entity_id

    var bookingId by BookedEntities.booking_id
    var booking by Booking referencedOn BookedEntities.booking_id
}
