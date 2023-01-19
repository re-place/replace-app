package replace.dto

import kotlin.reflect.KProperty1
import kotlinx.serialization.Serializable
import replace.model.BookableEntityType

@Serializable
data class BookableEntityTypeDto(
    override val id: String,
    val name: String,
    val bookableEntities: List<BookableEntityDto>? = null,
): ModelDto

fun BookableEntityType.toDto(with: List<KProperty1<BookableEntityType, *>> = emptyList()): BookableEntityTypeDto {
    val bookableEntities = if (with.contains(BookableEntityType::bookableEntities)) {
        bookableEntities.map { it.toDto() }
    } else {
        null
    }

    return BookableEntityTypeDto(
        id = id.value,
        name = name,
        bookableEntities = bookableEntities,
    )
}
