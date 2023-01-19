package replace.model

import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant

object TemporaryFiles : Models() {
    val name = varchar("name", 255)
    val path = varchar("path", 65535)
    val extension = varchar("extension", 255)
    val sizeInBytes = long("size_in_bytes")
    val mime = varchar("mime", 255).nullable()
    val createdAt = timestamp("created_at").default(Instant.now())
}

class TemporaryFile(id: EntityID<String>) : Model(id) {
    companion object : EntityClass<String, TemporaryFile>(TemporaryFiles)
    var name by TemporaryFiles.name
    var path by TemporaryFiles.path
    var extension by TemporaryFiles.extension
    var sizeInBytes by TemporaryFiles.sizeInBytes
    var mime by TemporaryFiles.mime
    var createdAt by TemporaryFiles.createdAt
}
