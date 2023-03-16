package replace.usecase.generator

import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.string
import io.kotest.property.arbitrary.uuid
import org.jetbrains.exposed.sql.transactions.transaction
import replace.dto.CreateSiteDto
import replace.dto.SiteDto
import replace.model.Site

fun ReplaceArb.site(): Arb<Site> = arbitrary {
    val name = Arb.string(1..100).bind()
    transaction {
        Site.new {
            this.name = name
        }
    }
}

fun ReplaceArb.siteDto(): Arb<SiteDto> = arbitrary {
    val id = Arb.uuid().bind().toString()
    val name = Arb.string(1..100).bind()
    SiteDto(id, name)
}

fun ReplaceArb.siteCreateDto(

): Arb<CreateSiteDto> = arbitrary {
    val name = Arb.string(1..100).bind()
    CreateSiteDto(name)
}
