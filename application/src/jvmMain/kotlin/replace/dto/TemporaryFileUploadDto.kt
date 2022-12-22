package replace.dto

import kotlinx.serialization.Serializable
import replace.model.TemporaryFileUpload
import java.time.LocalDateTime

@Serializable
class TemporaryFileUploadDto(
    override val id: String? = null,
    val name: String,
    val path: String,
    val mime: String,
    val extension: String,
    val sizeInBytes: Int,
    val createdAt: String,
) : Dto

fun TemporaryFileUpload.toDto() = TemporaryFileUploadDto(
    id = id?.toHexString(),
    name = name,
    path = path,
    mime = mime,
    extension = extension,
    sizeInBytes = sizeInBytes,
    createdAt = createdAt.toString()
)

fun TemporaryFileUploadDto.toModel() = TemporaryFileUpload(name, path, mime, extension, sizeInBytes, LocalDateTime.parse(createdAt))
