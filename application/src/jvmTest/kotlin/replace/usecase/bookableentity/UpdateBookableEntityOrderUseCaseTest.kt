package replace.usecase.bookableentity

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.next
import io.kotest.property.checkAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import replace.dto.UpdateBookableEntityOrderDto
import replace.model.BookableEntities
import replace.model.BookableEntity
import replace.usecase.generator.ReplaceArb
import replace.usecase.generator.bookableEntity
import replace.usecase.generator.floor
import replace.usecase.useDatabase

class UpdateBookableEntityOrderUseCaseTest : FunSpec(
    {
        context("happy path") {
            test("Update a simple floor") {
                useDatabase {
                    checkAll(5, ReplaceArb.floor()) { floor ->
                        val entities = Arb.list(ReplaceArb.bookableEntity(floorArb = Arb.constant(floor)), 1..10).next()
                        val shuffled = entities.shuffled()

                        newSuspendedTransaction {
                            BookableEntity.find { BookableEntities.floor_id eq floor.id }
                                .map { it.id.toString() }.sorted() shouldBe shuffled.map { it.id.toString() }.sorted()
                        }

                        UpdateBookableEntityOrderUseCase.execute(
                            UpdateBookableEntityOrderDto(
                                floorId = floor.id.toString(),
                                bookableEntityIds = shuffled.map { it.id.toString() },
                            ),
                        )

                        newSuspendedTransaction {
                            BookableEntity.find { BookableEntities.floor_id eq floor.id }
                                .sortedBy { it.index }
                                .map { it.id.toString() }
                                .shouldBe(shuffled.map { it.id.toString() })
                        }
                    }
                }
            }
        }
    },
)
