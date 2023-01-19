package replace.model

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.CustomFunction
import org.jetbrains.exposed.sql.VarCharColumnType

open class Models : IdTable<String>() {
    final override val id: Column<EntityID<String>> = varchar("id", 36).defaultExpression(CustomFunction("gen_random_uuid()", VarCharColumnType())).entityId()
    override val primaryKey = PrimaryKey(id, name = "pk_${tableName}_id")
}

abstract class Model(id: EntityID<String>) : Entity<String>(id)

// TODO: add https://github.com/JetBrains/Exposed/issues/497#issuecomment-520266191
