package replace.model

import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

object Sites : Models("sites") {
    val name = varchar("name", 255)
}

class Site(id: EntityID<String>) : Model(id) {
    companion object : EntityClass<String, Site>(Sites)
    var name by Sites.name

    val floors by Floor referrersOn Floors.site_id
}
