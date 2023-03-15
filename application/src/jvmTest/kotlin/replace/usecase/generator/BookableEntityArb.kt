package replace.usecase.generator

import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.orNull
import io.kotest.property.arbitrary.string
import io.kotest.property.arbitrary.uuid
import org.jetbrains.exposed.sql.transactions.transaction
import replace.dto.BookableEntityDto
import replace.dto.BookableEntityTypeDto
import replace.dto.CreateBookableEntityDto
import replace.dto.FloorDto
import replace.model.BookableEntity
import replace.model.BookableEntityType
import replace.model.Floor

fun ReplaceArb.bookableEntity(
    floorArb: Arb<Floor> = floor(),
    parentArb: Arb<BookableEntity?> = ReplaceArb.bookableEntity().orNull(0.75),
    typeArb: Arb<BookableEntityType> = bookableEntityType(),
): Arb<BookableEntity> = arbitrary {
    val name = Arb.string(1..100).bind()
    val posX = Arb.int(-100..100).bind()
    val posY = Arb.int(-100..100).bind()
    // TODO: Index range? @Jose
    val index = Arb.int(0..100).bind()
    val floor = floorArb.bind()
    val parentBookableEntity = parentArb.bind()
    val type = typeArb.bind()
    transaction {
        BookableEntity.new {
            this.name = name
            this.posX = posX
            this.posY = posY
            this.index = index
            this.floorId = floor.id
            this.parent = parentBookableEntity
            this.typeId = type.id
        }
    }
}

fun ReplaceArb.bookableEntityDto(
    floorArb: Arb<FloorDto> = floorDto(),
    parentArb: Arb<BookableEntityDto?> = ReplaceArb.bookableEntityDto().orNull(0.75),
    typeArb: Arb<BookableEntityTypeDto> = bookableEntityTypeDto(),
): Arb<BookableEntityDto> = arbitrary {
    val id = Arb.uuid().bind()
    val name = Arb.string(1..100).bind()
    val posX = Arb.int(-100..100).bind()
    val posY = Arb.int(-100..100).bind()
    // TODO: Index range? @Jose
    val index = Arb.int(0..100).bind()
    val floor = floorArb.bind()
    val parent = parentArb.bind()
    val type = typeArb.bind()
    BookableEntityDto(id.toString(), name, posX, posY, index, floor.id, parent?.id, type.id, floor, parent, type)
}

fun ReplaceArb.bookableEntityCreateDto(

): Arb<CreateBookableEntityDto> = arbitrary {
    val name = Arb.string(1..100).bind()
    val posX = Arb.int(-100..100).bind()
    val posY = Arb.int(-100..100).bind()
    CreateBookableEntityDto(name, posX, posY)
}
