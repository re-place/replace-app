package replace

import io.ktor.server.netty.EngineMain

object KtorBackend : Backend {
    override fun start(args: Array<String>): Unit = EngineMain.main(args)
}
