package replace

import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import replace.http.applicationModule

object KtorBackend : Backend {
    override fun start(args: Array<String>) {
        embeddedServer(
            Netty,
            port = 8000,
            module = Application::applicationModule
        ).start(wait = true)
    }
}
