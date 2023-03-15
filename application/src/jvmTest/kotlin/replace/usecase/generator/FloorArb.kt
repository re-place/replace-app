package replace.usecase.generator

import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.string
import io.kotest.property.arbitrary.uuid
import org.jetbrains.exposed.sql.transactions.transaction
import replace.dto.FloorDto
import replace.dto.SiteDto
import replace.model.Floor
import replace.model.Site

fun ReplaceArb.floor(
    siteArb: Arb<Site> = site(),
): Arb<Floor> = arbitrary {
    val name = Arb.string(1..100).bind()
    val site = siteArb.bind()
    transaction {
        Floor.new {
            this.name = name
            this.siteId = site.id
        }
    }
}

fun ReplaceArb.floorDto(
    siteArb: Arb<SiteDto> = siteDto(),
): Arb<FloorDto> = arbitrary {
    val id = Arb.uuid().bind()
    val name = Arb.string(1..100).bind()
    val site = siteArb.bind()
    FloorDto(id.toString(), name, site.id, site)
}
