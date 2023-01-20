package replace

object KtorBackend : Backend {
    override fun start(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)
}
