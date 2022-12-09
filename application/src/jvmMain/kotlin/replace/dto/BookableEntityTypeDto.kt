package replace.dto

import kotlinx.serialization.Serializable
import replace.model.BookableEntityType

@Serializable
data class BookableEntityTypeDto(
    override val id: String? = null,
    val name: String,
) : Dto

fun BookableEntityType.toDto() = BookableEntityTypeDto(
    id = _id?.toHexString(),
    name = name,
)