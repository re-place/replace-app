package replace

import io.ktor.server.application.*
import replace.plugin.SinglePageApplication

fun Application.applicationModule() {
    install(SinglePageApplication) {
        folderPath = "static"
        ignoreIfContains = Regex("^/api.*$")
    }
}
