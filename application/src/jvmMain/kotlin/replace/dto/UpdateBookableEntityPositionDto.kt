package replace.dto

import kotlinx.serialization.Serializable

@Serializable
data class UpdateBookableEntityPositionDto(
    val id: String,

    val posX: Int,
    val posY: Int,
)
