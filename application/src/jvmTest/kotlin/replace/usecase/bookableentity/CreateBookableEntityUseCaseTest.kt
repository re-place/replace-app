package replace.usecase.bookableentity

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll
import org.jetbrains.exposed.sql.transactions.transaction
import replace.dto.CreateBookableEntityDto
import replace.model.BookableEntity
import replace.usecase.generator.ReplaceArb
import replace.usecase.generator.bookableEntityDto
import replace.usecase.generator.bookableEntityType
import replace.usecase.generator.floor
import replace.usecase.prepareDatabase

class CreateBookableEntityUseCaseTest : FunSpec({
    context("happy path") {
        test("create a simple bookable entity") {
            prepareDatabase()
            checkAll(ReplaceArb.bookableEntityDto()) { bookableEntityDto ->
                val fromUseCase = CreateBookableEntityUseCase.execute(bookableEntityDto)

                fromUseCase.id shouldNotBe null
                fromUseCase.name shouldBe bookableEntityName
                fromUseCase.posX shouldBe bookableEntityTypePosX
                fromUseCase.posY shouldBe bookableEntityTypePosY
                fromUseCase.floorId shouldBe floor.id.value
                fromUseCase.typeId shouldBe entityType.id.value
                fromUseCase.index shouldBe 0

                val fromDb = transaction {
                    BookableEntity.findById(fromUseCase.id)
                }

                fromDb shouldNotBe null
                fromDb!!

                fromDb.id shouldNotBe null
                fromDb.name shouldBe bookableEntityName
                fromDb.posX shouldBe bookableEntityTypePosX
                fromDb.posY shouldBe bookableEntityTypePosY
                fromDb.floorId shouldBe floor.id.value
                fromDb.typeId shouldBe entityType.id.value
                fromDb.index shouldBe 0
            }
        }
    }
})
