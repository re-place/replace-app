package replace.usecase.bookableentity

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.property.checkAll
import org.jetbrains.exposed.sql.transactions.transaction
import replace.model.BookableEntity
import replace.usecase.generator.ReplaceArb
import replace.usecase.generator.bookableEntityCreateDto
import replace.usecase.prepareDatabase
import java.util.UUID

class CreateBookableEntityUseCaseTest : FunSpec({
    context("happy path") {
        test("create a simple bookable entity") {
            prepareDatabase()
            checkAll(ReplaceArb.bookableEntityCreateDto()) { dto ->
                val fromUseCase = CreateBookableEntityUseCase.execute(dto)

                fromUseCase.id shouldNotBe null
                shouldNotThrowAny { UUID.fromString(fromUseCase.id) }
                fromUseCase.name shouldBe dto.name
                fromUseCase.posX shouldBe dto.posX
                fromUseCase.posY shouldBe dto.posY
                fromUseCase.floorId shouldBe dto.floorId
                fromUseCase.typeId shouldBe dto.typeId
                fromUseCase.index shouldBe dto.index

                val fromDb = transaction {
                    BookableEntity.findById(fromUseCase.id)
                }

                fromDb shouldNotBe null
                fromDb!!

                fromDb.id shouldNotBe null
                fromDb.name shouldBe dto.name
                fromDb.posX shouldBe dto.posX
                fromDb.posY shouldBe dto.posY
                fromDb.floorId.toString() shouldBe dto.floorId
                fromDb.typeId.toString() shouldBe dto.typeId
                fromDb.index shouldBe dto.index
            }
        }
    }
})
