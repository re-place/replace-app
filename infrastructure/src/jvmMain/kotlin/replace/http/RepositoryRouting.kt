package replace.http

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.flow.toList
import replace.datastore.Repository

context(Route)
fun <T : Any> routeRepository(repository: Repository<T>) {
    get {
        call.respond(repository.getAll().toList())
    }
}
