package replace.dto

import kotlinx.serialization.Serializable
import replace.model.Floor

@Serializable
class FloorDto(
    override val id: String? = null,
    val name: String,
    val locationId: String,
) : Dto

fun Floor.toDto() = FloorDto(
    id = id?.toHexString(),
    name = name,
    locationId = locationId.toHexString(),
)
