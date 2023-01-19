package replace.dto

import kotlin.reflect.KProperty1
import kotlinx.serialization.Serializable
import replace.model.BookableEntity

@Serializable
data class UpdateBookableEntityDto(
    val id: String,

    val name: String,

    val floorId: String,

    val parentId: String? = null,

    val typeId: String? = null,
)
