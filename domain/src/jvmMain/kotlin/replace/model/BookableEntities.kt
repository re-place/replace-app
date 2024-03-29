package replace.model

import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

object BookableEntities : Models("bookable_entities") {
    val name = varchar("name", 255)
    val pos_x = integer("pos_x").default(0)
    val pos_y = integer("pos_y").default(0)
    val index = integer("index").default(0)
    val floor_id = reference("floor_id", Floors)
    val type_id = reference("type_id", BookableEntityTypes).nullable()
    val parent_id = reference("parent_id", BookableEntities).nullable()
}

class BookableEntity(id: EntityID<String>) : Model(id) {
    companion object : EntityClass<String, BookableEntity>(BookableEntities)
    var name by BookableEntities.name

    var posX by BookableEntities.pos_x
    var posY by BookableEntities.pos_y

    var index by BookableEntities.index

    var floorId by BookableEntities.floor_id
    var floor by Floor referencedOn BookableEntities.floor_id

    var parentId by BookableEntities.parent_id
    var parent by BookableEntity optionalReferencedOn BookableEntities.parent_id

    var typeId by BookableEntities.type_id
    var type by BookableEntityType optionalReferencedOn BookableEntities.type_id

    val children by BookableEntity optionalReferrersOn BookableEntities.parent_id

    val bookings by Booking via BookedEntities
}
