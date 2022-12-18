package replace.http

import guru.zoroark.tegral.openapi.dsl.schema
import guru.zoroark.tegral.openapi.ktor.describe
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
import replace.dto.Dto
import replace.http.controller.Routing
import replace.model.ObjectWithId
import kotlin.reflect.typeOf

inline fun <reified T : ObjectWithId, reified D : Dto> Route.routeRepository(repository: Repository<T>, crossinline toDto: (T) -> D) {
    val listType = typeOf<List<D>>()

    get {
        try {
            call.respond(repository.getAll().map(toDto))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    } describe {
        description = "Gets all ${T::class.simpleName}s"
        200 response {
            description = "All ${T::class.simpleName}s"
            json {
                schema(listType)
            }
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
            call.respond(toDto(dbResult))
        }
    } describe {
        description = "Gets a ${T::class.simpleName} by id"
        "id" pathParameter {
            description = "The id of the ${T::class.simpleName}"
            schema(ObjectId().toString())
        }
        200 response {
            description = "The ${T::class.simpleName} with the given id"
            json {
                schema<D>()
            }
        }
        400 response {
            description = "The id is not a valid ObjectId"
        }
        404 response {
            description = "No ${T::class.simpleName} with the given id exists"
        }
    }

    delete<Routing.ById> { route ->
        if (!ObjectId.isValid(route.id)) {
            return@delete call.respondText("Id ${route.id} is not a valid ObjectId", status = HttpStatusCode.BadRequest)
        }
        call.respond(repository.deleteOneById(ObjectId(route.id)))
    } describe {
        description = "Deletes a ${T::class.simpleName} by id"
        "id" pathParameter {
            description = "The id of the ${T::class.simpleName}"
            schema(ObjectId().toString())
        }
        200 response {
            description = "The deleted ${T::class.simpleName} with the given id"
            plainText {
                schema(true)
            }
        }
        400 response {
            description = "The id is not a valid ObjectId"
        }
    }
}
