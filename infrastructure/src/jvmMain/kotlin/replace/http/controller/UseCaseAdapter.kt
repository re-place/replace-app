package replace.http.controller

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.util.pipeline.PipelineContext

suspend inline fun <reified T : Any> PipelineContext<Unit, ApplicationCall>.executeUseCase(useCase: () -> T) {
    try {
        call.respond(useCase())
    } catch (e: IllegalStateException) {
        call.respondText("Invalid request: ${e.message}", status = HttpStatusCode.BadRequest)
    } catch (e: Exception) {
        call.respondText("An internal error occurred: ${e.message}", status = HttpStatusCode.InternalServerError)
    }
}
