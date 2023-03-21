package replace.usecase.booking

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.next
import io.kotest.property.arbitrary.set
import io.kotest.property.checkAll
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import replace.dto.BookingDto
import replace.model.Booking
import replace.usecase.generator.ReplaceArb
import replace.usecase.generator.bookableEntity
import replace.usecase.generator.bookingCreateDto
import replace.usecase.generator.floor
import replace.usecase.generator.timeStamp
import replace.usecase.generator.user
import replace.usecase.useDatabase
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

class GetBookingUseCaseTest : FunSpec(
    {
        context("happy path") {
            test("get bookings for self") {
                useDatabase {
                    val floor = ReplaceArb.floor().next()
                    checkAll(
                        3,
                        Arb.set(ReplaceArb.bookableEntity(floorArb = Arb.constant(floor)), 10..20),
                        Arb.set(ReplaceArb.bookableEntity(floorArb = Arb.constant(floor)), 10..20),
                        ReplaceArb.user(),
                        ReplaceArb.user(),
                        Arb.timeStamp().filter { it > Clock.System.now() + 100.days },
                    ) { bookableEntitiesA, bookableEntitiesB, userA, userB, pivot ->

                        data class DayBookings(
                            val bookingsForA: Map<BookingDto, List<String>>,
                            val bookingsForB: Map<BookingDto, List<String>>,
                        ) {
                            val all: Map<BookingDto, List<String>> = bookingsForA + bookingsForB
                        }

                        suspend fun createDayBookings(
                            startArb: Arb<Instant>,
                        ): DayBookings {
                            val (forA, forB) = listOf(bookableEntitiesA, bookableEntitiesB).map { entities ->
                                entities.chunked(3).map { toBook ->
                                    ReplaceArb.bookingCreateDto(
                                        Arb.constant(toBook.map { it.id.value }),
                                        startArb,
                                    ) { startActual ->
                                        Arb.timeStamp().filter { it > startActual + 1.hours && it < startActual + 5.hours }
                                    }.next()
                                }
                            }
                            return DayBookings(
                                bookingsForA = forA.associate {
                                    CreateBookingUseCase.execute(
                                        it,
                                        userA.id.toString(),
                                    ) to it.bookedEntityIds
                                },
                                bookingsForB = forB.associate {
                                    CreateBookingUseCase.execute(
                                        it,
                                        userB.id.toString(),
                                    ) to it.bookedEntityIds
                                },
                            )
                        }

                        val beforePivotBookings = createDayBookings(
                            Arb.timeStamp().filter { it < pivot - 14.days && it > pivot - 15.days },
                        )

                        val afterPivotBookings = createDayBookings(
                            Arb.timeStamp().filter { it > pivot + 14.days && it < pivot + 15.days },
                        )

                        (beforePivotBookings.all + afterPivotBookings.all).forEach { (bookingDto, expectedBookableEntities) ->
                            val booking = newSuspendedTransaction {
                                Booking.findById(bookingDto.id)
                            }
                            booking shouldNotBe null
                            booking!!

                            newSuspendedTransaction {
                                booking.bookedEntities.map { it.id.value } shouldBe expectedBookableEntities
                            }
                        }

                        // before pivot

                        // get OWN bookings
                        beforePivotBookings.bookingsForA.forEach { (bookingDto, expectedBookableEntities) ->
                            expectedBookableEntities.map { expected ->
                                val booking = newSuspendedTransaction {
                                    GetBookingUseCase.execute(
                                        true,
                                        userA.id.toString(),
                                        floor.id.toString(),
                                        expected,
                                        bookingDto.start,
                                        bookingDto.end,
                                    )
                                }

                                booking shouldHaveSize 1
                                booking[0].id shouldBe bookingDto.id
                            }
                        }

                        // get OTHER bookings
                        beforePivotBookings.bookingsForB.forEach { (bookingDto, expectedBookableEntities) ->
                            expectedBookableEntities.map { expected ->
                                val booking = newSuspendedTransaction {
                                    GetBookingUseCase.execute(
                                        false,
                                        userA.id.toString(),
                                        floor.id.toString(),
                                        expected,
                                        bookingDto.start,
                                        bookingDto.end,
                                    )
                                }

                                booking shouldHaveSize 1
                                booking[0].id shouldBe bookingDto.id
                            }
                        }

                        // after pivot

                        // get OWN bookings
                        afterPivotBookings.bookingsForA.forEach { (bookingDto, expectedBookableEntities) ->
                            expectedBookableEntities.map { expected ->
                                val booking = newSuspendedTransaction {
                                    GetBookingUseCase.execute(
                                        true,
                                        userA.id.toString(),
                                        floor.id.toString(),
                                        expected,
                                        bookingDto.start,
                                        bookingDto.end,
                                    )
                                }

                                booking shouldHaveSize 1
                                booking[0].id shouldBe bookingDto.id
                            }
                        }

                        // get OTHER bookings
                        afterPivotBookings.bookingsForB.forEach { (bookingDto, expectedBookableEntities) ->
                            expectedBookableEntities.map { expected ->
                                val booking = newSuspendedTransaction {
                                    GetBookingUseCase.execute(
                                        false,
                                        userA.id.toString(),
                                        floor.id.toString(),
                                        expected,
                                        bookingDto.start,
                                        bookingDto.end,
                                    )
                                }

                                booking shouldHaveSize 1
                                booking[0].id shouldBe bookingDto.id
                            }
                        }
                    }
                }
            }
        }
    },
)
