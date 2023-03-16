package replace.usecase.generator

import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.string
import io.kotest.property.arbitrary.uuid
import org.jetbrains.exposed.sql.transactions.transaction
import replace.dto.CreateFloorDto
import replace.dto.FloorDto
import replace.dto.SiteDto
import replace.dto.UpdateFloorDto
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
    val id = Arb.uuid().bind().toString()
    val name = Arb.string(1..100).bind()
    val site = siteArb.bind()
    FloorDto(id, name, site.id, site)
}

fun ReplaceArb.floorCreateDto(
    siteArb: Arb<SiteDto> = siteDto()
    ):Arb<CreateFloorDto> = arbitrary {
    val id = Arb.uuid().bind().toString()
    val name = Arb.string(1..100).bind()
    val site = siteArb.bind()
    CreateFloorDto(name, site.id, null)
}

fun ReplaceArb.floorUpdateDto(
    siteArb: Arb<SiteDto> = siteDto()
):Arb<UpdateFloorDto> = arbitrary {
    val id = Arb.uuid().bind().toString()
    val name = Arb.string(1..100).bind()
    val site = siteArb.bind()
    UpdateFloorDto(id, name, site.id, null)
}
