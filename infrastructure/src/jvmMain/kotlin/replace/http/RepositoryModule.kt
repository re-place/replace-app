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
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.sql.transactions.transaction
import replace.dto.ModelDto
import replace.http.controller.Routing
import replace.model.Model
import kotlin.reflect.typeOf

inline fun <reified T : Model, reified D : ModelDto> Route.routeRepository(repository: EntityClass<String, T>, crossinline toDto: (T) -> D) {
    val listType = typeOf<List<D>>()

    get {
        call.respond(transaction { repository.all().map(toDto) })
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
        val model = transaction { repository.findById(route.id) }

        if (model === null) {
            call.respondText("No Model with id ${route.id} found", status = HttpStatusCode.NotFound)
            return@get
        }

        call.respond(toDto(model))
    } describe {
        description = "Gets a ${T::class.simpleName} by id"
        "id" pathParameter {
            description = "The id of the ${T::class.simpleName}"
            schema("<id>")
        }
        200 response {
            description = "The ${T::class.simpleName} with the given id"
            json {
                schema<D>()
            }
        }
        404 response {
            description = "No ${T::class.simpleName} with the given id exists"
        }
    }

    delete<Routing.ById> { route ->
        val model = transaction { repository.findById(route.id) }

        if (model === null) {
            return@delete
        }

        model.delete()

        call.respondText("Deleted Model with id ${route.id}", status = HttpStatusCode.OK)
    } describe {
        description = "Deletes a ${T::class.simpleName} by id"
        "id" pathParameter {
            description = "The id of the ${T::class.simpleName}"
            schema("<id>")
        }
        200 response {
            description = "The deleted ${T::class.simpleName} with the given id"
            plainText {
                schema(true)
            }
        }
    }
}
