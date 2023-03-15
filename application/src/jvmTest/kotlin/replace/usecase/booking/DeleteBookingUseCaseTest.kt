package replace.usecase.booking

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.property.checkAll
import org.jetbrains.exposed.sql.transactions.transaction
import replace.model.Booking
import replace.usecase.generator.ReplaceArb
import replace.usecase.generator.booking
import replace.usecase.generator.user
import replace.usecase.prepareDatabase

class DeleteBookingUseCaseTest : FunSpec({
    context("happy path") {
        test("delete a simple bookable entity") {
            prepareDatabase()
            checkAll(ReplaceArb.booking()) { booking ->
                val user = transaction {
                    Booking.findById(booking.id) shouldNotBe null
                    booking.user
                }
                DeleteBookingUseCase.execute(booking.id.toString(), user.id.toString())
                transaction {
                    Booking.findById(booking.id) shouldBe null
                }
            }
        }
    }
},)
