package replace.dto

import kotlin.reflect.KProperty1
import kotlinx.serialization.Serializable
import replace.model.Floor

@Serializable
class FloorDto(
    override val id: String,
    val name: String,
    val siteId: String,
    val site: SiteDto? = null,
    val planFileId: String? = null,
    val planFile: FileUploadDto? = null,
    val bookableEntities: List<BookableEntityDto>? = null,
    val url: String,
) : ModelDto

fun Floor.toDto(with: List<KProperty1<Floor, *>> = emptyList()): FloorDto {

    val planFile = planFileId?.let {
        FileUploadDto(
            fileId = it.value,
            temporary = false,
        )
    }

    val siteDto = if (with.contains(Floor::site)) {
        site.toDto()
    } else {
        null
    }

    val bookableEntities = if (with.contains(Floor::bookableEntities)) {
        bookableEntities.map { it.toDto() }
    } else {
        null
    }

    return FloorDto(
        id = id.value,
        name = name,
        siteId = siteId.value,
        planFile = planFile,
        site = siteDto,
        bookableEntities = bookableEntities,
        url = "test"
    )
}
