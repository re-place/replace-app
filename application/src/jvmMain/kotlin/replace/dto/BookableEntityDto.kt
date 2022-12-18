package replace.dto

import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import replace.model.BookableEntity

@Serializable
data class BookableEntityDto(
    override val id: String? = null,
    val name: String,
    val floorId: String,
    val parentId: String? = null,
    val type: BookableEntityTypeDto? = null
) : Dto

fun BookableEntity.toDto() = BookableEntityDto(
    id = id?.toHexString(),
    name = name,
    floorId = floorId.toHexString(),
    parentId = parentId?.toHexString(),
    type = type?.toDto()
)

fun BookableEntityDto.toModel() =
    BookableEntity(
        name,
        type?.toModel(),
        ObjectId(floorId),
        if (parentId != null) ObjectId(parentId) else null
    )
