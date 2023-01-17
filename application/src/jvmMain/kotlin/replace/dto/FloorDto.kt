package replace.dto

import kotlinx.serialization.Serializable
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

    val planFile = planFileId?.let {
        FileUploadDto(
            id = it.value,
            temporary = false,
        )
    }

    return FloorDto(
        id = id.value,
        name = name,
        siteId = siteId.value,
        planFile = planFile,
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
