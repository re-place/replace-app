package replace.usecase.booking

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldNotBe
import io.kotest.property.checkAll
import org.jetbrains.exposed.sql.transactions.transaction
import replace.model.BookableEntity
import replace.usecase.generator.ReplaceArb
import replace.usecase.generator.bookingCreateDto
import replace.usecase.generator.user
import replace.usecase.useDatabase

class GetBookingUseCaseTest : FunSpec({
    context("happy path") {
        test("get the bookings") {
            useDatabase {
                checkAll(10, ReplaceArb.user(), ReplaceArb.bookingCreateDto()) { user, dto ->
                    CreateBookingUseCase.execute(dto, user.id.toString())

                    val bookableEntity = transaction {
                        BookableEntity.findById(dto.bookedEntityIds[0])
                    }
                    bookableEntity shouldNotBe null
                    bookableEntity!!
                    GetBookingUseCase.execute(true, user.id.value, bookableEntity.floorId.toString(), bookableEntity.id.value, dto.start, dto.end) shouldNotBe null
                }
            }
        }
    }
},)
