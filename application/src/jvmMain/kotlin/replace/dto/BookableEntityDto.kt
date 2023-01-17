package replace.dto

import kotlinx.serialization.Serializable
import replace.model.BookableEntity

@Serializable
data class BookableEntityDto(
    override val id: String? = null,
    val name: String,

    val floorId: String,

    val parentId: String? = null,

    val typeId: String? = null,
) : Dto

fun BookableEntity.toDto(): BookableEntityDto = BookableEntityDto(
    id = id.value,
    name = name,
    floorId = floorId.value,
    parentId = parentId?.value,
    typeId = typeId?.value,
)
