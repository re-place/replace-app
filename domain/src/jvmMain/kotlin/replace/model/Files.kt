package replace.model

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

object Files : Models() {
    val name = varchar("name", 255)
    val path = varchar("path", 65535)
    val extension = varchar("extension", 255)
    val sizeInBytes = long("size_in_bytes")
    val mime = varchar("mime", 255).nullable()
}

class File(id: EntityID<String>) : Model(id) {
    companion object : EntityClass<String, File>(Files)
    var name by Files.name
    var path by Files.path
    var extension by Files.extension
    var sizeInBytes by Files.sizeInBytes
    var mime by Files.mime
}
