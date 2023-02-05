package replace.dto

import kotlinx.serialization.Serializable

@Serializable
data class UpdateBookableEntityTypeDto(
    val id: String,
    val name: String,
)
