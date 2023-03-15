package replace.usecase.generator

import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.flatMap
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.string
import io.kotest.property.arbitrary.uuid
import org.jetbrains.exposed.sql.transactions.transaction
import replace.dto.BookableEntityDto
import replace.dto.BookableEntityTypeDto
import replace.dto.CreateBookableEntityDto
import replace.dto.FloorDto
import replace.dto.toDto
import replace.model.BookableEntity
import replace.model.BookableEntityType
import replace.model.Floor

fun ReplaceArb.bookableEntity(
    floorArb: Arb<Floor> = floor(),
    parentArb: Arb<BookableEntity?> =
        Arb.boolean().flatMap { b -> if (b) ReplaceArb.bookableEntity() else Arb.constant(null) },
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
    floorArb: Arb<FloorDto> = floor().map { it.toDto() },
    parentArb: Arb<BookableEntityDto?> =
        Arb.boolean().flatMap { b -> if (b) ReplaceArb.bookableEntity().map { it.toDto() } else Arb.constant(null) },
    typeArb: Arb<BookableEntityTypeDto> = bookableEntityType().map { it.toDto() },
): Arb<BookableEntityDto> = arbitrary {
    val id = Arb.uuid().bind().toString()
    val name = Arb.string(1..100).bind()
    val posX = Arb.int(-100..100).bind()
    val posY = Arb.int(-100..100).bind()
    // TODO: Index range? @Jose
    val index = Arb.int(0..100).bind()
    val floor = floorArb.bind()
    val parent = parentArb.bind()
    val type = typeArb.bind()
    BookableEntityDto(id, name, posX, posY, index, floor.id, parent?.id, type.id, floor, parent, null, type)
}

fun ReplaceArb.bookableEntityCreateDto(
    floorIdArb: Arb<String> = floor().map { it.id.toString() },
    parentIdArb: Arb<String?> =
        Arb.boolean().flatMap { b -> if (b) ReplaceArb.bookableEntity().map { it.id.toString() } else Arb.constant(null) },
    typeIdArb: Arb<String> = bookableEntityType().map { it.id.toString() },
): Arb<CreateBookableEntityDto> = arbitrary {
    val name = Arb.string(1..100).bind()
    val posX = Arb.int(-100..100).bind()
    val posY = Arb.int(-100..100).bind()
    val floorId = floorIdArb.bind()
    val parentId = parentIdArb.bind()
    val typeId = typeIdArb.bind()
    val index = Arb.int(0..100).bind()
    CreateBookableEntityDto(name, posX, posY, floorId, parentId, typeId, index)
}
