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
import replace.usecase.generator.ReplaceArb
import replace.usecase.generator.floor
import replace.usecase.useDatabase

class DeleteFloorUseCaseTest : FunSpec(
    {
        context("happy path") {
            test("delete a simple floor") {
                useDatabase {
                    val storage = InMemoryFileStorage()
                    checkAll(2, ReplaceArb.floor()) { floor ->
                        DeleteFloorUseCase.execute(floor.id.value, storage) shouldBe null
                        val bookableEntities = transaction {
                            BookableEntity.find(BookableEntities.floor_id eq floor.id)
                        }
//                            bookableEntities shouldBe null ??
                        bookableEntities.shouldHaveSize(0)
                    }
                }
            }
        }
    },
)
