package replace.usecase.floor

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.property.checkAll
import org.jetbrains.exposed.sql.transactions.transaction
import replace.datastore.InMemoryFileStorage
import replace.model.Floor
import replace.usecase.generator.ReplaceArb
import replace.usecase.generator.floorCreateDto
import replace.usecase.useDatabase

class CreateFloorUseCaseTest : FunSpec(
    {
        context("happy path") {
            test("create a simple floor") {
                useDatabase {
                    val storage = InMemoryFileStorage()
                    checkAll(20, ReplaceArb.floorCreateDto()) { dto ->
                        val fromUseCase = CreateFloorUseCase.execute(dto, storage)

                        fromUseCase.id shouldNotBe null
                        fromUseCase.name shouldBe dto.name

                        val fromDb = transaction {
                            Floor.findById(fromUseCase.id)
                        }

                        fromDb shouldNotBe null
                        fromDb!!

                        fromDb.id shouldNotBe null
                        fromDb.name shouldBe dto.name
                    }
                }
            }
        }
    },
)
