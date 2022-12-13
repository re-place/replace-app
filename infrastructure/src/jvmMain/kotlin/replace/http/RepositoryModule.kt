package replace.http

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.resources.delete
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import org.bson.types.ObjectId
import replace.datastore.Repository
import replace.http.controller.Routing
import replace.model.ObjectWithId

inline fun <reified T : ObjectWithId> Route.routeRepository(repository: Repository<T>) {
    get {
        try {
            call.respond(repository.getAll().toTypedArray())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    get<Routing.ById> { route ->
        if (!ObjectId.isValid(route.id)) {
            return@get call.respondText("Id ${route.id} is not a valid ObjectId", status = HttpStatusCode.BadRequest)
        }
        val dbResult = repository.findOneById(ObjectId(route.id))
        if (dbResult == null) {
            call.respondText("No document with id ${route.id}", status = HttpStatusCode.NotFound)
        } else {
            call.respond(dbResult)
        }
    }
    delete<Routing.ById> { route ->
        if (!ObjectId.isValid(route.id)) {
            return@delete call.respondText("Id ${route.id} is not a valid ObjectId", status = HttpStatusCode.BadRequest)
        }
        call.respond(repository.deleteOneById(ObjectId(route.id)))
    }
}
