package replace.dto

import kotlinx.serialization.Serializable
import replace.model.BookableEntity

@Serializable
data class BookableEntityDto(
    override val id: String? = null,
    val name: String,
    val parentId: String? = null,
) : Dto

fun BookableEntity.toDto() = BookableEntityDto(
    id = _id?.toHexString(),
    name = name,
    parentId = parentId?.toHexString(),
)