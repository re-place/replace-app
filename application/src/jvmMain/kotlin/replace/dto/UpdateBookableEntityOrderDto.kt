package replace.dto

import kotlinx.serialization.Serializable

@Serializable
data class UpdateBookableEntityOrderDto(
    val floorId: String,
    val bookableEntityIds: List<String>,
)
