package replace

import io.ktor.server.application.Application
import io.ktor.server.application.install
import replace.plugin.SinglePageApplication

fun Application.applicationModule() {
    install(SinglePageApplication) {
        folderPath = "static"
        ignoreIfContains = Regex("^/api.*$")
    }
}
