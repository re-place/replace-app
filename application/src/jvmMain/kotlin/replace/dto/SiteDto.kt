package replace.dto

import kotlinx.serialization.Serializable
import replace.model.Site

@Serializable
class SiteDto(
    override val id: String? = null,
    val name: String,
) : Dto

fun Site.toDto() = SiteDto(
    id = id?.toHexString(),
    name = name,
)
