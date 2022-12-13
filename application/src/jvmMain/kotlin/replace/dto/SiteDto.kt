package replace.dto

import replace.model.Site

class SiteDto(
    override val id: String? = null,
    val name: String,
) : Dto

fun Site.toDto() = SiteDto(
    id = _id?.toHexString(),
    name = name,
)
