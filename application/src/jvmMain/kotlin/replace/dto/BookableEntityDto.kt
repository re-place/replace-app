package replace.dto

import kotlinx.serialization.Serializable
import replace.model.BookableEntity
import kotlin.reflect.KProperty1

@Serializable
data class BookableEntityDto(
    override val id: String,

    val name: String,

    val floorId: String,

    val parentId: String? = null,

    val typeId: String? = null,

    val floor: FloorDto? = null,
    val parent: BookableEntityDto? = null,
    val type: BookableEntityTypeDto? = null,
) : ModelDto

fun BookableEntity.toDto(with: List<KProperty1<BookableEntity, *>> = emptyList()): BookableEntityDto {
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

    return BookableEntityDto(
        id = id.value,
        name = name,
        floorId = floorId.value,
        parentId = parentId?.value,
        typeId = typeId?.value,
        floor = floor,
        parent = parent,
        type = type,
    )
}
