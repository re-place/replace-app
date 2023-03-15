package replace.usecase.generator

import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.string
import io.kotest.property.arbitrary.uuid
import org.jetbrains.exposed.sql.transactions.transaction
import replace.dto.BookableEntityTypeDto
import replace.model.BookableEntityType

fun ReplaceArb.bookableEntityType(): Arb<BookableEntityType> = arbitrary {
    val name = Arb.string(1..100).bind()
    transaction {
        BookableEntityType.new {
            this.name = name
        }
    }
}

fun ReplaceArb.bookableEntityTypeDto(): Arb<BookableEntityTypeDto> = arbitrary {
    val id = Arb.uuid().bind()
    val name = Arb.string(1..100).bind()
    BookableEntityTypeDto(id.toString(), name)
}
