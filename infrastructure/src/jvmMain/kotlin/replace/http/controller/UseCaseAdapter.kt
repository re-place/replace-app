package replace.http.controller

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.util.pipeline.PipelineContext
import java.lang.IllegalArgumentException

suspend inline fun <reified T : Any> PipelineContext<Unit, ApplicationCall>.executeUseCase(useCase: () -> T) {
    try {
        call.respond(useCase())
    } catch (e: IllegalStateException) {
        call.respondText(
            "Invalid request: ${e.message} \n" +
                " ${e.stackTrace.joinToString("\n")}",
            status = HttpStatusCode.BadRequest
        )
    } catch (e: BadRequestException) {
        call.respondText(
            "Invalid request: ${e.cause?.message ?: e.message} \n" +
                " ${e.stackTrace.joinToString("\n")}",
            status = HttpStatusCode.BadRequest
        )
    } catch (e: IllegalArgumentException) {
        call.respondText(
            "Invalid request: ${e.cause?.message ?: e.message} \n" +
                " ${e.stackTrace.joinToString("\n")}",
            status = HttpStatusCode.BadRequest
        )
    } catch (e: Exception) {
        call.respondText("[${e::class}]: An internal error occurred: ${e.message} \n ${e.stackTrace.joinToString("\n")}", status = HttpStatusCode.InternalServerError)
    }
}
