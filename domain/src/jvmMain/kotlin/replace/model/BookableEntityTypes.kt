package replace.model

import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

object BookableEntityTypes : Models("bookable_entity_types") {
    val name = varchar("name", 255)
}

class BookableEntityType(id: EntityID<String>) : Model(id) {
    companion object : EntityClass<String, BookableEntityType>(BookableEntityTypes)
    var name by BookableEntityTypes.name

    val bookableEntities by BookableEntity optionalReferrersOn BookableEntities.type_id
}
