package replace.usecase.floor

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldHaveAtLeastSize
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.property.checkAll
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import replace.datastore.InMemoryFileStorage
import replace.model.BookableEntities
import replace.model.BookableEntity
import replace.usecase.bookableentity.CreateBookableEntityUseCase
import replace.usecase.generator.ReplaceArb
import replace.usecase.generator.bookableEntityCreateFloorDto
import replace.usecase.generator.floor
import replace.usecase.useDatabase

class DeleteAllBookingsOfFloorUseCaseTest : FunSpec(
    {
        context("happy path") {
            test("delete a simple floor") {
                useDatabase {
                    val storage = InMemoryFileStorage()
                    checkAll(1, ReplaceArb.floor()) { floor ->
                        checkAll(1, ReplaceArb.bookableEntityCreateFloorDto(floor.id.value)) { dto ->
                            val fromUseCase = CreateBookableEntityUseCase.execute(dto)

                            transaction {
                                val bookableEntities = BookableEntity.find(BookableEntities.floor_id eq floor.id)
                                bookableEntities.shouldHaveAtLeastSize(1)
                            }

                            DeleteFloorUseCase.execute(floor.id.value, storage)

                            transaction {
                                val bookableEntities = BookableEntity.find(BookableEntities.floor_id eq floor.id)
                                bookableEntities.shouldHaveSize(0)
                            }
                        }
                    }
                }
            }
        }
    },
)
