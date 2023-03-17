package replace.dto

import kotlinx.serialization.Serializable
import replace.model.BookableEntity
import kotlin.reflect.KProperty1

@Serializable
data class BookableEntityDto(
    override val id: String,

    val name: String,

    val posX: Int,
    val posY: Int,

    val index: Int,

    val floorId: String,

    val parentId: String? = null,

    val typeId: String? = null,

    val floor: FloorDto? = null,
    val parent: BookableEntityDto? = null,
    val children: List<BookableEntityDto>? = null,
    val type: BookableEntityTypeDto? = null,
) : ModelDto

suspend fun BookableEntity.toDto(with: List<KProperty1<BookableEntity, *>> = emptyList()): BookableEntityDto {
    val floor = if (with.contains(BookableEntity::floor)) {
        floor.toDto()
    } else {
        null
    }

    val parent = if (with.contains(BookableEntity::parent)) {
        parent?.toDto()
    } else {
        null
    }

    val type = if (with.contains(BookableEntity::type)) {
        type?.toDto()
    } else {
        null
    }

    val children = if (with.contains(BookableEntity::children)) {
        children.map { it.toDto() }
    } else {
        null
    }

    return BookableEntityDto(
        id = id.value,
        name = name,
        posX = posX,
        posY = posY,
        index = index,
        floorId = floorId.value,
        parentId = parentId?.value,
        typeId = typeId?.value,
        floor = floor,
        parent = parent,
        children = children,
        type = type,
    )
}
