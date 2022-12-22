package replace.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class TemporaryFileUpload(
    val name: String,
    val path: String,
    val mime: String,
    val extension: String,
    val sizeInBytes: Int,
    @Contextual val createdAt: LocalDateTime,
) : ObjectWithId()
