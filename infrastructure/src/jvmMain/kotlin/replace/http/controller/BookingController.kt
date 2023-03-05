package replace.http.controller

import guru.zoroark.tegral.openapi.dsl.schema
import guru.zoroark.tegral.openapi.ktor.describe
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.resources.delete
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.transactions.transaction
import replace.dto.BookingDto
import replace.dto.CreateBookingDto
import replace.dto.toDto
import replace.http.routeRepository
import replace.http.withUserSession
import replace.model.Booking
import replace.usecase.booking.CreateBookingUseCase
import replace.usecase.booking.DeleteBookingUseCase
import replace.usecase.booking.GetBookingUseCase
import kotlin.reflect.typeOf

fun Route.registerBookingRoutes() {
    route("/api/booking") {
        val listType = typeOf<List<BookingDto>>()

        get {
            call.respond(
                transaction {
                    Booking.all().with(Booking::bookedEntities).map { it.toDto(listOf(Booking::bookedEntities)) }
                }
            )
        } describe {
            description = "Gets all Bookings"
            200 response {
                description = "All Bookings"
                json {
                    schema(listType)
                }
            }
        }

        delete<Routing.ById> { route ->
            withUserSession {
                DeleteBookingUseCase.execute(route.id, it.userId)

                call.respond(HttpStatusCode.NoContent)
            }
        } describe {
            description = "Deletes a Booking by id"
            "id" pathParameter {
                description = "The id of the Booking to delete"
                schema("<id>")
            }
            204 response {
                description = "The deleted Booking with the given id"
            }
        }

        routeRepository(Booking.Companion) {
            it.toDto()
        }

        get("/byParams") {
            executeUseCase {
                withUserSession {
                    val start = call.parameters["start"]
                    val end = call.parameters["end"]
                    val bookableEntityId = call.parameters["bookableEntityId"]
                    val floorId = call.parameters["floorId"]

                    val my = call.parameters["my"]?.toBoolean()

                    GetBookingUseCase.execute(my, it.userId, floorId, bookableEntityId, start, end)
                }
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
            "bookableEntityId" queryParameter {
                description = "The id of a bookable entity"
                required = false
                schema(typeOf<String>())
            }
            "floorId" queryParameter {
                description = "The id of a floor"
                required = false
                schema(typeOf<String>())
            }
            "my" queryParameter {
                description = "If true, only bookings of the current user are returned"
                required = false
                schema(typeOf<Boolean>())
            }
            200 response {
                description = "The bookings by Date from User"
                json {
                    schema(typeOf<List<BookingDto>>())
                }
            }
        }

        post<CreateBookingDto> { dto ->
            executeUseCase {
                withUserSession {
                    CreateBookingUseCase.execute(dto, it.userId)
                }
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
