package replace.dto

import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import replace.model.Floor

@Serializable
class FloorDto(
    override val id: String? = null,
    val name: String,
    val siteId: String,
) : Dto

fun Floor.toDto() = FloorDto(
    id = id?.toHexString(),
    name = name,
    siteId = siteId.toHexString(),
)

fun FloorDto.toModel() = Floor(name, ObjectId(siteId))
