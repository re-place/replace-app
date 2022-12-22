package replace.model

import kotlinx.serialization.Serializable

@Serializable
data class File(
    val name: String,
    val path: String,
    val mime: String,
    val extension: String,
    val sizeInBytes: Int,
) : ObjectWithId()
