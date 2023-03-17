package replace.usecase.bookableentity

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.property.checkAll
import org.jetbrains.exposed.sql.transactions.transaction
import replace.datastore.InMemoryFileStorage
import replace.model.BookableEntity
import replace.usecase.generator.ReplaceArb
import replace.usecase.generator.bookableEntity
import replace.usecase.generator.bookableEntityUpdateDto
import replace.usecase.useDatabase

class UpdateBookableEntityUseCaseTest : FunSpec(
    {
        context("happy path") {
            test("Update a simple bookable entity") {
                useDatabase {
                    checkAll(5, ReplaceArb.bookableEntity()) { original ->
                        checkAll(5, ReplaceArb.bookableEntityUpdateDto(original.id.toString())) { update ->
                            val fromUseCase = UpdateBookableEntityUseCase.execute(update)

                            fromUseCase.id shouldBe update.id
                            fromUseCase.name shouldBe update.name
                            fromUseCase.floorId shouldBe update.floorId
                            fromUseCase.parentId shouldBe update.parentId
                            fromUseCase.typeId shouldBe update.typeId
                            fromUseCase.posX shouldBe update.posX
                            fromUseCase.posY shouldBe update.posY
                            fromUseCase.index shouldBe update.index

                            val fromDB = transaction {
                                BookableEntity.findById(update.id)
                            }
                            fromDB shouldNotBe null
                            fromDB!!

                            fromDB.id.toString() shouldBe update.id
                            fromDB.name shouldBe update.name
                            fromDB.floorId.toString() shouldBe update.floorId
                            fromDB.parentId?.toString() shouldBe update.parentId
                            fromDB.typeId?.toString() shouldBe update.typeId
                            fromDB.posX shouldBe update.posX
                            fromDB.posY shouldBe update.posY
                            fromDB.index shouldBe update.index

                        }
                    }
                }
            }
        }
    },
)
