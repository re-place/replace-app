package replace.dto

import kotlinx.serialization.Serializable
import replace.model.File

@Serializable
class FileDto(
    override val id: String? = null,
    val name: String,
    val path: String,
    val mime: String,
    val extension: String,
    val sizeInBytes: Int,
) : Dto

fun File.toDto() = FileDto(
    id = id?.toHexString(),
    name = name,
    path = path,
    mime = mime,
    extension = extension,
    sizeInBytes = sizeInBytes,
)

fun FileDto.toModel() = File(name, path, mime, extension, sizeInBytes)
