package replace.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateBookableEntityDto(
    val name: String,

    val floorId: String,

    val parentId: String? = null,

    val typeId: String? = null,
)
