package replace.dto

import kotlin.reflect.KProperty1
import kotlinx.serialization.Serializable
import replace.datastore.FileStorage
import replace.model.Floor

@Serializable
data class CreateFloorDto(
    val name: String,
    val planFile: FileUploadDto? = null,
)
