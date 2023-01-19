package replace.dto

import kotlin.reflect.KProperty1
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.transactions.transaction
import replace.model.Site

@Serializable
class SiteDto(
    override val id: String,
    val name: String,
    val floors: List<FloorDto>? = null,
) : ModelDto

fun Site.toDto(with: List<KProperty1<Site, *>> = emptyList()): SiteDto {
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
