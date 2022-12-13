package replace.http.controller

import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.litote.kmongo.coroutine.CoroutineDatabase
import replace.datastore.MongoRepository
import replace.dto.SiteDto
import replace.http.routeRepository
import replace.model.Site
import replace.usecase.site.CreateSiteUseCase

fun Route.registerSiteRoutes(db: CoroutineDatabase) {
    val siteRepository = MongoRepository<Site>(db.getCollection())

    route("/api/site") {
        routeRepository(siteRepository)
        post<SiteDto> {
            executeUseCase {
                CreateSiteUseCase.execute(it, siteRepository)
            }
        }
    }
}
