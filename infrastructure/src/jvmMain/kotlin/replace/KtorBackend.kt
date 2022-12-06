package replace

import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

object KtorBackend : Backend {
    override fun start(args: Array<String>) {
        embeddedServer(
            Netty,
            port = 8000,
            module = Application::applicationModule
        ).start(wait = true)
    }
}
