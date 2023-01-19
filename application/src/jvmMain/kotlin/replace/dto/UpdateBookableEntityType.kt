package replace.dto

import kotlin.reflect.KProperty1
import kotlinx.serialization.Serializable
import replace.model.BookableEntityType

@Serializable
data class UpdateBookableEntityTypeDto(
    val id: String,
    val name: String,
)
