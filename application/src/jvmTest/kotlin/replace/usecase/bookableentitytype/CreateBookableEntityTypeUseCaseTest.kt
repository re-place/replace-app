package replace.usecase.bookableentitytype

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.property.checkAll
import org.jetbrains.exposed.sql.transactions.transaction
import replace.model.BookableEntityType
import replace.usecase.generator.ReplaceArb
import replace.usecase.generator.bookableEntityTypeCreateDto
import replace.usecase.useDatabase
import java.util.UUID

class CreateBookableEntityTypeUseCaseTest : FunSpec(
    {
        context("happy path") {
            test("create a simple bookable entity type") {
                useDatabase {
                    checkAll(ReplaceArb.bookableEntityTypeCreateDto()) { dto ->
                        val fromUseCase = CreateBookableEntityTypeUseCase.execute(dto)

                        fromUseCase.id shouldNotBe null
                        shouldNotThrowAny { UUID.fromString(fromUseCase.id) }
                        fromUseCase.name shouldBe dto.name

                        val fromDb = transaction {
                            BookableEntityType.findById(fromUseCase.id)
                        }

                        fromDb shouldNotBe null
                        fromDb!!

                        fromDb.id.toString() shouldBe fromUseCase.id
                        fromDb.name shouldBe fromUseCase.name
                    }
                }
            }
        }
    },
)
