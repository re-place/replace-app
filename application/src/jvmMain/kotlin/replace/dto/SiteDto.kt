package replace.dto

import kotlinx.serialization.Serializable
import replace.model.Site
import kotlin.reflect.KProperty1

@Serializable
class SiteDto(
    override val id: String,
    val name: String,
    val floors: List<FloorDto>? = null,
) : ModelDto

suspend fun Site.toDto(with: List<KProperty1<Site, *>> = emptyList()): SiteDto {
    val floors = if (with.contains(Site::floors)) {
        floors.map { it.toDto() }
    } else {
        null
    }

    return SiteDto(
        id = id.value,
        name = name,
        floors = floors,
    )
}
