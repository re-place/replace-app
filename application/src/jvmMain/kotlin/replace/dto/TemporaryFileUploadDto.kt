package replace.dto

import kotlinx.serialization.Serializable
import replace.model.TemporaryFile
import java.time.LocalDateTime

@Serializable
class TemporaryFileUploadDto(
    override val id: String? = null,
    val name: String,
    val path: String,
    val mime: String? = null,
    val extension: String,
    val sizeInBytes: Long,
    val createdAt: String,
) : Dto

fun TemporaryFile.toDto() = TemporaryFileUploadDto(
    id = id?.toHexString(),
    name = name,
    path = path,
    mime = mime,
    extension = extension,
    sizeInBytes = sizeInBytes,
    createdAt = createdAt.toString()
)

fun TemporaryFileUploadDto.toModel() = TemporaryFile(
    name = name,
    path = path,
    extension = extension,
    mime = mime,
    sizeInBytes = sizeInBytes,
    createdAt = LocalDateTime.parse(createdAt)
)
