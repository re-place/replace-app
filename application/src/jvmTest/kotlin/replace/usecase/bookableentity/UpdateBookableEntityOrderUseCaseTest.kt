package replace.usecase.bookableentity

import io.kotest.core.spec.style.FunSpec
import io.kotest.property.checkAll
import replace.usecase.generator.ReplaceArb
import replace.usecase.generator.bookableEntity
import replace.usecase.generator.bookableEntityOrderUpdateDto
import replace.usecase.useDatabase

class UpdateBookableEntityOrderUseCaseTest : FunSpec(
    {
        context("happy path") {
            test("Update a simple floor") {
                useDatabase {
                    checkAll(5, ReplaceArb.bookableEntity()) { original ->
                        checkAll(5, ReplaceArb.bookableEntityOrderUpdateDto(original.floorId.value.toString())) { update ->

                        }
                    }
                }
            }
        }
    },
)
