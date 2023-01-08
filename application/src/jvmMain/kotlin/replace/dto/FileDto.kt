package replace.dto

import kotlinx.serialization.Serializable
import replace.model.File

@Serializable
class FileDto(
    override val id: String? = null,
    val name: String,
    val path: String,
    val extension: String,
    val sizeInBytes: Long,
    val mime: String? = null,
) : Dto

fun File.toDto() = FileDto(
    id = id?.toHexString(),
    name = name,
    path = path,
    extension = extension,
    sizeInBytes = sizeInBytes,
    mime = mime,
)

fun FileDto.toModel() = File(
    name = name,
    path = path,
    extension = extension,
    sizeInBytes = sizeInBytes,
    mime = mime,
)
