package replace.usecase.floor

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.property.checkAll
import org.jetbrains.exposed.sql.transactions.transaction
import replace.datastore.InMemoryFileStorage
import replace.model.Floor
import replace.usecase.generator.ReplaceArb
import replace.usecase.generator.floor
import replace.usecase.generator.floorUpdateDto
import replace.usecase.useDatabase

class UpdateFloorUseCaseTest : FunSpec(
    {
        context("happy path") {
            test("Update a simple floor") {
                useDatabase {
                    checkAll(20, ReplaceArb.floor()){ floor ->
                        val storage = InMemoryFileStorage()
                        checkAll(ReplaceArb.floorUpdateDto(floor.id.value)) { dto ->
                            val updatedFloor = UpdateFloorUseCase.execute(dto, storage)

                            val fromDb = transaction {
                                Floor.findById(updatedFloor.id)
                            }

                            fromDb shouldNotBe null
                            fromDb!!

                            fromDb.id.value shouldBe updatedFloor.id.toString()
                            fromDb.name shouldBe updatedFloor.name
                            fromDb.siteId.value shouldBe updatedFloor.siteId.toString()
                        }
                    }
                }
            }
        }
    },
)
