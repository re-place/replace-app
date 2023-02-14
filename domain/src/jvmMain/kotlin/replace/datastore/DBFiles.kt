package replace.datastore

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

object DBFiles : IdTable<String>("file_storage") {
    override val id: Column<EntityID<String>> = varchar("id", 65535).entityId()
    override val primaryKey = PrimaryKey(id, name = "pk_file_storage_id")

    val data = binary("data")
}

class DBFile(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, DBFile>(DBFiles)
    var data by DBFiles.data
}
