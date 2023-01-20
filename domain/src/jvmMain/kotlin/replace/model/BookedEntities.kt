package replace.model

import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

object BookedEntities : Models("booked_entities") {
    val bookable_entity_id = reference("bookable_entity_id", BookableEntities)
    val booking_id = reference("booking_id", Bookings)
}

class BookedEntity(id: EntityID<String>) : Model(id) {
    companion object : EntityClass<String, BookedEntity>(BookedEntities)

    var bookableEntityId by BookedEntities.bookable_entity_id
    var bookableEntity by BookableEntity referencedOn BookedEntities.bookable_entity_id

    var bookingId by BookedEntities.booking_id
    var booking by Booking referencedOn BookedEntities.booking_id
}
