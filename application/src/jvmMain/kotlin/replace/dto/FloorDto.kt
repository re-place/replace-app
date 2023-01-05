package replace.dto

import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import replace.datastore.FileRepository
import replace.datastore.FileStorage
import replace.datastore.TemporaryFileRepository
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
    return Floor(
        name = name,
        siteId = ObjectId(siteId),
        planFileId = planFile?.let { ObjectId(planFile.id) },
    )
}

suspend fun FloorDto.saveFiles(
    temporaryFileRepository: TemporaryFileRepository,
    fileRepository: FileRepository,
    fileStorage: FileStorage
): FloorDto {
    return FloorDto(
        id = id,
        name = name,
        siteId = siteId,
        planFile = planFile?.save(
            temporaryFileRepository = temporaryFileRepository,
            fileRepository = fileRepository,
            fileStorage = fileStorage,
        ),
    )
}
