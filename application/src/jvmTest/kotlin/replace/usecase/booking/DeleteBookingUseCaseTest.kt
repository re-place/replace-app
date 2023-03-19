package replace.usecase.booking

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.property.checkAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import replace.model.Booking
import replace.usecase.generator.ReplaceArb
import replace.usecase.generator.booking
import replace.usecase.useDatabase

class DeleteBookingUseCaseTest : FunSpec(
    {
        context("happy path") {
            test("delete a simple booking") {
                useDatabase {
                    checkAll(3, ReplaceArb.booking()) { booking ->
                        val user = newSuspendedTransaction {
                            Booking.findById(booking.id) shouldNotBe null
                            booking.user
                        }
                        DeleteBookingUseCase.execute(booking.id.toString(), user.id.toString())
                        newSuspendedTransaction {
                            Booking.findById(booking.id) shouldBe null
                        }
                    }
                }
            }
        }
    },
)
