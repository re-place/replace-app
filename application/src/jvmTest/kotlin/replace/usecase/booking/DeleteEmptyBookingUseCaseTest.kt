package replace.usecase.booking

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.set
import io.kotest.property.checkAll
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import replace.model.Booking
import replace.usecase.generator.ReplaceArb
import replace.usecase.generator.booking
import replace.usecase.generator.bookingCreateDto
import replace.usecase.generator.user
import replace.usecase.tables
import replace.usecase.useDatabase

class DeleteEmptyBookingUseCaseTest : FunSpec(
    {
        context("happy path") {
            test("all bookings are empty") {
                useDatabase {
                    checkAll(5, Arb.set(ReplaceArb.booking(), 0..5)) { bookings ->
                        newSuspendedTransaction {
                            Booking.all().map { it.id } shouldBe bookings.map { it.id }
                        }
                        DeleteEmptyBookingsUseCase.execute()
                        newSuspendedTransaction {
                            Booking.all().toSet() shouldBe emptySet()
                        }
                    }
                }
            }
            test("some bookings are empty") {
                useDatabase {
                    checkAll(
                        3,
                        Arb.set(ReplaceArb.bookingCreateDto(), 5..10),
                        ReplaceArb.user(),
                        Arb.int(1..4),
                    ) { createDtos, user, makeEmptyCount ->
                        // assume CreateBookingUseCase works (based on other tests)
                        val bookings = createDtos.map { dto -> CreateBookingUseCase.execute(dto, user.id.toString()) }
                        newSuspendedTransaction {
                            Booking.all().toSet().map { it.id.toString() } shouldBe bookings.map { it.id }
                        }
                        DeleteEmptyBookingsUseCase.execute()
                        newSuspendedTransaction {
                            Booking.all().toSet().map { it.id.toString() } shouldBe bookings.map { it.id }
                        }

                        val mutableBookings = bookings.toMutableList()

                        for (i in 0 until makeEmptyCount) {
                            newSuspendedTransaction {
                                val booking = Booking.findById(mutableBookings.removeFirst().id)
                                booking shouldNotBe null
                                booking!!

                                booking.bookedEntities.forEach { it.delete() }
                            }
                        }

                        DeleteEmptyBookingsUseCase.execute()

                        mutableBookings.size shouldBe bookings.size - makeEmptyCount

                        newSuspendedTransaction {
                            Booking.all().toSet().map { it.id.toString() } shouldBe mutableBookings.map { it.id }
                        }

                        transaction {
                            SchemaUtils.drop(*tables)
                            SchemaUtils.create(*tables)
                        }
                    }
                }
            }
        }
    },
)
