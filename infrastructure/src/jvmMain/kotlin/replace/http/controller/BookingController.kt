package replace.http.controller

import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.litote.kmongo.coroutine.CoroutineDatabase
import replace.datastore.MongoRepository
import replace.dto.BookingDto
import replace.http.routeRepository
import replace.model.Booking
import replace.usecase.booking.CreateBookingUseCase

fun Route.registerBookingRoutes(db: CoroutineDatabase) {
    val bookingRepository = MongoRepository<Booking>(db.getCollection())

    route("/api/booking") {
        routeRepository(bookingRepository)
        post<BookingDto> {
            executeUseCase {
                CreateBookingUseCase.execute(it, bookingRepository)
            }
        }
    }
}
