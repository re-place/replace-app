package replace

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

object KtorBackend : Backend {
    override fun start(args: Array<String>) {
        embeddedServer(
            Netty,
            port = 8000,
            module = Application::myApplicationModule
        ).start(wait = true)
    }
}

fun Application.myApplicationModule() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
    }
}
