package replace.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateBookableEntityTypeDto(
    val name: String,
)
