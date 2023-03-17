package replace.dto

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import replace.model.Floor
import kotlin.reflect.KProperty1

@Serializable
class FloorDto(
    override val id: String,
    val name: String,
    val siteId: String,
    val site: SiteDto? = null,
    val planFileId: String? = null,
    val planFile: FileDto? = null,
    val bookableEntities: List<BookableEntityDto>? = null,
) : ModelDto

suspend fun Floor.toDto(with: List<KProperty1<Floor, *>> = emptyList()): FloorDto {
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
        planFile = newSuspendedTransaction { planFile?.toDto() },
        site = siteDto,
        bookableEntities = bookableEntities,
    )
}
