package replace.http.controller

import guru.zoroark.tegral.openapi.dsl.schema
import guru.zoroark.tegral.openapi.ktor.describe
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.litote.kmongo.coroutine.CoroutineDatabase
import replace.datastore.MongoBookableEntityRepository
import replace.datastore.MongoRepository
import replace.dto.BookingDto
import replace.dto.toDto
import replace.http.routeRepository
import replace.model.Booking
import replace.usecase.booking.CreateBookingUseCase

fun Route.registerBookingRoutes(db: CoroutineDatabase) {
    val bookingRepository = MongoRepository<Booking>(db.getCollection())
    val bookableEntityRepository = MongoBookableEntityRepository(db.getCollection())

    route("/api/booking") {
        routeRepository(bookingRepository) {
            it.toDto()
        }

        post<BookingDto> {
            executeUseCase {
                CreateBookingUseCase.execute(it, bookingRepository, bookableEntityRepository)
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
