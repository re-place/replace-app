package replace.usecase.bookableentitytype

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.property.checkAll
import org.jetbrains.exposed.sql.transactions.transaction
import replace.dto.CreateBookableEntityTypeDto
import replace.model.BookableEntityType
import replace.usecase.prepareDatabase

class CreateBookableEntityTypeUseCaseTest : FunSpec({
    context("happy path") {
        test("create a simple bookable entity type") {
            prepareDatabase()
            checkAll { bookableEntityTypeName: String ->
                val createDto = CreateBookableEntityTypeDto(
                    name = bookableEntityTypeName,
                )
                val fromUseCase = CreateBookableEntityTypeUseCase.execute(createDto)

                fromUseCase.id shouldNotBe null
                fromUseCase.name shouldBe bookableEntityTypeName

                val fromDb = transaction {
                    BookableEntityType.findById(fromUseCase.id)
                }

                fromDb shouldNotBe null
                fromDb!!

                fromDb.id shouldBe fromUseCase.id
                fromDb.name shouldBe fromUseCase.name
            }
        }
    }
})
