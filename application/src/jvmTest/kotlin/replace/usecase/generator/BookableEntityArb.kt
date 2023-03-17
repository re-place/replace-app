package replace.usecase.generator

import io.kotest.property.Arb
import io.kotest.property.arbitrary.*
import org.jetbrains.exposed.sql.transactions.transaction
import replace.dto.*
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

fun ReplaceArb.bookableEntityUpdateDto(
    id: String,
    floorIdArb: Arb<String> = floor().map { it.id.toString() },
    parentIdArb: Arb<String?> =
        Arb.boolean().flatMap { b -> if (b) ReplaceArb.bookableEntity().map { it.id.toString() } else Arb.constant(null) },
    typeIdArb: Arb<String> = bookableEntityType().map { it.id.toString() },
): Arb<UpdateBookableEntityDto> = arbitrary {
    val name = Arb.string(1..100).bind()
    val posX = Arb.int(-100..100).bind()
    val posY = Arb.int(-100..100).bind()
    val floorId = floorIdArb.bind()
    val parentId = parentIdArb.bind()
    val typeId = typeIdArb.bind()
    val index = Arb.int(0..100).bind()
    UpdateBookableEntityDto(id, name, floorId, parentId, typeId, posX, posY, index)
}

fun ReplaceArb.bookableEntityOrderUpdateDto(
    floorIdArb: Arb<String> = floor().map { it.id.toString() },
): Arb<UpdateBookableEntityOrderDto> = arbitrary {
    val floorId = floorIdArb.bind()
    val bookEntities = emptyList<String>().toMutableList()
    val counter = Arb.int(5..10).bind()
    for (i in 0..counter){
        var  bent = ReplaceArb.bookableEntity().next()
        bookEntities += bent.id.toString()
    }
    UpdateBookableEntityOrderDto(floorId,bookEntities)
}
