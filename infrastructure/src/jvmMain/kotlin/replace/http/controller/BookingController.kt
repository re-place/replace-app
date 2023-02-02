package replace.http.controller

import guru.zoroark.tegral.openapi.dsl.schema
import guru.zoroark.tegral.openapi.ktor.describe
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.sessions.get
import io.ktor.server.sessions.sessions
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.transactions.transaction
import replace.dto.BookingDto
import replace.dto.CreateBookingDto
import replace.dto.toDto
import replace.http.UserSession
import replace.http.routeRepository
import replace.model.Booking
import replace.usecase.booking.CreateBookingUseCase
import replace.usecase.booking.GetBookingUseCase
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

        get("/byDate") {
            executeUseCase {
                val start = call.parameters["start"]
                val end = call.parameters["end"]

                val userId = call.sessions.get<UserSession>()?.email!!

                GetBookingUseCase.execute(start, end, userId)
            }
        } describe {
            "start" queryParameter {
                description = "The start timestamp"
                required = false
                schema(typeOf<String>())
            }
            "end" queryParameter {
                description = "The end timestamp"
                required = false
                schema(typeOf<String>())
            }
            200 response {
                description = "The bookings by Date from User"
                json {
                    schema(typeOf<List<BookingDto>>())
                }
            }
        }

        post<CreateBookingDto> {
            executeUseCase {
                val userId = call.sessions.get<UserSession>()?.email!!

                CreateBookingUseCase.execute(it, userId)
            }
        } describe {
            body {
                json {
                    schema<BookingDto>()
                }
            }
            description = "Creates a new booking"
            body {
                json {
                    schema<CreateBookingDto>()
                }
            }
            200 response {
                description = "The created booking"
                json {
                    schema<BookingDto>()
                }
            }
        }
    }
}
