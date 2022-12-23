package replace.dto

import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import replace.model.Floor

@Serializable
class FloorDto(
    override val id: String? = null,
    val name: String,
    val siteId: String,
    val planFile: FileUploadDto? = null,
) : Dto

fun Floor.toDto(): FloorDto {

    val fileId = planFileId?.toHexString()
        ?: return FloorDto(
            id = id?.toHexString(),
            name = name,
            siteId = siteId.toHexString(),
        )

    return FloorDto(
        id = id?.toHexString(),
        name = name,
        siteId = siteId.toHexString(),
        planFile = FileUploadDto(
            id = fileId,
            temporary = false,
        ),
    )
}

fun FloorDto.toModel(): Floor {
    if (planFile != null && !planFile.temporary) {
        return Floor(
            name = name,
            siteId = ObjectId(siteId),
            planFileId = ObjectId(planFile.id),
        )
    }

    return Floor(
        name = name,
        siteId = ObjectId(siteId),
    )
}
