package replace.model

import kotlinx.serialization.Serializable

@Serializable
data class File(
    val name: String,
    val path: String,
    val extension: String,
    val sizeInBytes: Long,
    val mime: String? = null,
) : ObjectWithId()
