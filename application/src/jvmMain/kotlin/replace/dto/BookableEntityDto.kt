package replace.dto

import kotlinx.serialization.Serializable
import replace.model.BookableEntityWithId

@Serializable
data class BookableEntityDto(
    override val id: String? = null,
    val name: String,
    val typeId: String,
    val floorId: String,
    val parentId: String? = null,
) : Dto

fun BookableEntityWithId.toDto() = BookableEntityDto(
    id = id.toHexString(),
    name = name,
    typeId = type.id.toHexString(),
    floorId = floorId.toHexString(),
    parentId = parentId?.toHexString(),
)
