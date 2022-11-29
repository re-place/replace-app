package replace

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

object KtorBackend : Backend {
    override fun start(args: Array<String>) {
        embeddedServer(
            Netty,
            port = 8000,
            module = Application::applicationModule,
        ).start(wait = true)
    }
}
