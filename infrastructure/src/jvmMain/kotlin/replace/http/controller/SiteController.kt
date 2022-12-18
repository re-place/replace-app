package replace.http.controller

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import org.bson.types.ObjectId
import org.litote.kmongo.coroutine.CoroutineDatabase
import replace.datastore.MongoFloorRepository
import replace.datastore.MongoRepository
import replace.dto.SiteDto
import replace.dto.toDto
import replace.http.routeRepository
import replace.model.Site
import replace.usecase.site.CreateSiteUseCase
import replace.usecase.site.UpdateSiteUseCase

fun Route.registerSiteRoutes(db: CoroutineDatabase) {
    val siteRepository = MongoRepository<Site>(db.getCollection())
    val floorRepository = MongoFloorRepository(db.getCollection())

    route("/api/site") {
        routeRepository(siteRepository) {
            it.toDto()
        }

        post<SiteDto> {
            executeUseCase {
                CreateSiteUseCase.execute(it, siteRepository)
            }
        }

        put<SiteDto> {
            executeUseCase {
                UpdateSiteUseCase.execute(it, siteRepository)
            }
        }

        get("/{siteId}/floor") {
            val siteId = call.parameters["siteId"] ?: return@get call.respondText("Missing id", status = HttpStatusCode.BadRequest)

            if (!ObjectId.isValid(siteId)) {
                return@get call.respondText("Id $siteId is not a valid ObjectId", status = HttpStatusCode.BadRequest)
            }

            val floors = floorRepository.findBySiteId(ObjectId(siteId)).map() { it.toDto() }

            call.respond(floors)
        }
    }
}
