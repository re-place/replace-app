package replace.http.controller

import io.ktor.server.routing.Route
import io.ktor.server.routing.route
import org.litote.kmongo.coroutine.CoroutineDatabase
import replace.datastore.MongoRepository
import replace.http.routeRepository
import replace.model.Booking

fun Route.registerBookingRoutes(db: CoroutineDatabase) {
    val bookingRepository = MongoRepository<Booking>(db.getCollection())

    route("/api/booking") {
        routeRepository(bookingRepository)
    }
}
