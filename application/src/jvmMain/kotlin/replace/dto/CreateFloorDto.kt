package replace.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateFloorDto(
    val name: String,
    val siteId: String,
    val planFile: FileUploadDto? = null,
)
