package replace.model

import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

object Floors : Models("floors") {
    val name = varchar("name", 255)
    val site_id = reference("site_id", Sites)
    val plan_file_id = reference("plan_file_id", Files).nullable()
}

class Floor(id: EntityID<String>) : Model(id) {
    companion object : EntityClass<String, Floor>(Floors)
    var name by Floors.name

    var siteId by Floors.site_id
    var site by Site referencedOn Floors.site_id

    var planFileId by Floors.plan_file_id
    var planFile by File optionalReferencedOn Floors.plan_file_id

    val bookableEntities by BookableEntity referrersOn BookableEntities.floor_id
}
