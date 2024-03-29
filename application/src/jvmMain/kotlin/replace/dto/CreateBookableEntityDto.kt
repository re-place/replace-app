package replace.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateBookableEntityDto(
    val name: String,

    val posX: Int,
    val posY: Int,

    val floorId: String,

    val parentId: String? = null,

    val typeId: String? = null,

    val index: Int,
)
