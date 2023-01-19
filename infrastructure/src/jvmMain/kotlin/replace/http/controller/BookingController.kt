package replace.http.controller

import guru.zoroark.tegral.openapi.dsl.schema
import guru.zoroark.tegral.openapi.ktor.describe
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import replace.dto.BookingDto
import replace.dto.CreateBookingDto
import replace.dto.toDto
import replace.http.routeRepository
import replace.model.Booking
import replace.usecase.booking.CreateBookingUseCase

fun Route.registerBookingRoutes() {
    route("/api/booking") {
        routeRepository(Booking.Companion) {
            it.toDto()
        }

        post<CreateBookingDto> {
            executeUseCase {
                CreateBookingUseCase.execute(it)
            }
        } describe {
            description = "Creates a new booking"
            body {
                json {
                    schema<BookingDto>()
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
