package replace

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.flow.toList
import replace.datastore.Repository
import replace.model.Booking

fun Application.configureRouting(
    bookingRepository: Repository<Booking>,
) {
    routing {
        route("/api/booking") {
            post {
                println("POST /api/booking")
                val booking = try {
                    call.receive<Booking>()
                } catch (e: Exception) {
                    println("Error: ${e.message}")
                    call.respondText("Error: ${e.message}")
                    return@post
                }
                println("Received booking: $booking")
                val insertedBooking = bookingRepository.insertOne(booking)
                if (insertedBooking != null) {
                    call.respond(insertedBooking)
                } else {
                    call.respondText("Could not insert booking")
                }
            }
            get {
                call.respond(bookingRepository.getAll().toList())
            }
        }
    }
}
