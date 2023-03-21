package replace.usecase.floor

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import replace.datastore.InMemoryFileStorage
import replace.model.BookableEntities
import replace.model.BookableEntity
import replace.model.Floor
import replace.usecase.generator.ReplaceArb
import replace.usecase.generator.floor
import replace.usecase.useDatabase

class DeleteFloorUseCaseTest : FunSpec(
    {
        context("happy path") {
            test("delete a simple floor") {
                useDatabase {
                    val storage = InMemoryFileStorage()
                    checkAll(20, ReplaceArb.floor()) { floor ->
                        DeleteFloorUseCase.execute(floor.id.value, storage)
                        transaction {
                            Floor.findById(floor.id.value) shouldBe null
                        }

                        transaction {
                            val bookableEntities = BookableEntity.find(BookableEntities.floor_id eq floor.id)
                            bookableEntities.shouldHaveSize(0)
                        }
                    }
                }
            }
        }
    },
)
