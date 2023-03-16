package replace.usecase.bookableentity

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.property.checkAll
import org.jetbrains.exposed.sql.transactions.transaction
import replace.datastore.InMemoryFileStorage
import replace.model.BookableEntity
import replace.usecase.generator.ReplaceArb
import replace.usecase.generator.bookableEntityUpdateDto
import replace.usecase.useDatabase

class UpdateBookableEntityUseCaseTest : FunSpec(
    {
        context("happy path") {
            test("Update a simple bookable entity") {
                useDatabase {
                    val storage = InMemoryFileStorage()
                    checkAll(ReplaceArb.bookableEntityUpdateDto()) { dto ->
                        val updatedBookableEntity = UpdateBookableEntityUseCase.execute(dto)
                        val fromDB = transaction {
                            BookableEntity.findById(updatedBookableEntity.id)
                        }
                        fromDB shouldNotBe null
                        fromDB!!

                        fromDB.id shouldBe dto.id
                        fromDB.name shouldBe dto.name
                        fromDB.floorId shouldBe dto.floorId
                        fromDB.parentId shouldBe dto.parentId
                        fromDB.typeId shouldBe dto.typeId
                        fromDB.posX shouldBe dto.posX
                        fromDB.posY shouldBe dto.posY
                        fromDB.index shouldBe dto.index
                    }
                }
            }
        }
    },
)
