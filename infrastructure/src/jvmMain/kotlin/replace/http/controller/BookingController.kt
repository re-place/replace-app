package replace.http.controller

import guru.zoroark.tegral.openapi.ktor.describe
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.transactions.transaction
import replace.dto.BookingDto
import replace.dto.toDto
import replace.http.routeRepository
import replace.model.Booking
import kotlin.reflect.typeOf

fun Route.registerBookingRoutes() {
    route("/api/booking") {
        val listType = typeOf<List<BookingDto>>()

        get {
            call.respond(transaction { Booking.all().with(Booking::bookedEntities).map { it.toDto(listOf(Booking::bookedEntities)) } })
        } describe {
            description = "Gets all Bookings"
            200 response {
                description = "All Bookings"
                json {
                    schema(listType)
                }
            }
        }
        routeRepository(Booking.Companion) {
            it.toDto()
        }

//        post<CreateBookingDto> {
//            executeUseCase {
//                val userId = call.sessions.get<UserSession>()?.userId!!
//
//                CreateBookingUseCase.execute(it, userId)
//            }
//        } describe {
//            body {
//                json {
//                    schema<BookingDto>()
//                }
//            }
//            description = "Creates a new booking"
//            body {
//                json {
//                    schema<CreateBookingDto>()
//                }
//            }
//            200 response {
//                description = "The created booking"
//                json {
//                    schema<BookingDto>()
//                }
//            }
//        }
    }
}
