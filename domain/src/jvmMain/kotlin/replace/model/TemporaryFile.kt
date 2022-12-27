package replace.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class TemporaryFile(
    val name: String,
    val path: String,
    val extension: String,
    val sizeInBytes: Long,
    val mime: String? = null,
    @Contextual val createdAt: LocalDateTime,
) : ObjectWithId()
