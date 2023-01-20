package replace.dto

import kotlinx.serialization.Serializable
import replace.model.File

@Serializable
data class FileDto(
    override val id: String,
    val name: String,
    val path: String,
    val extension: String,
    val sizeInBytes: Long,
    val mime: String? = null,
    val url: String,
) : ModelDto

fun File.toDto() = FileDto(
    id = id.value,
    name = name,
    path = path,
    extension = extension,
    sizeInBytes = sizeInBytes,
    mime = mime,
    url = "/api/file/$id"
)
