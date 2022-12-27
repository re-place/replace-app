package replace.dto

import kotlinx.serialization.Serializable
import replace.model.File

@Serializable
class FileDto(
    override val id: String? = null,
    val name: String,
    val path: String,
    val mime: String? = null,
    val extension: String,
    val sizeInBytes: Long,
) : Dto

fun File.toDto() = FileDto(
    id = id?.toHexString(),
    name = name,
    path = path,
    mime = mime,
    extension = extension,
    sizeInBytes = sizeInBytes,
)

fun FileDto.toModel() = File(
    name = name,
    path = path,
    extension = extension,
    mime = mime,
    sizeInBytes = sizeInBytes,
)
