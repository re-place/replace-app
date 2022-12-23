package replace.dto

import kotlinx.serialization.Serializable

@Serializable
class FileUploadDto(
    override val id: String,
    val temporary: Boolean,
) : Dto
