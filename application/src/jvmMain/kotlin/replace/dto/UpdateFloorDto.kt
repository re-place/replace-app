package replace.dto

import kotlinx.serialization.Serializable

@Serializable
data class UpdateFloorDto(
    val id: String,
    val name: String,
    val siteId: String,
    val planFile: FileUploadDto? = null,
)
