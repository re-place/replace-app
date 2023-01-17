package replace.http.controller

import guru.zoroark.tegral.openapi.dsl.schema
import guru.zoroark.tegral.openapi.ktor.describe
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
import replace.model.Sites
import replace.usecase.site.CreateSiteUseCase
import replace.usecase.site.UpdateSiteUseCase

fun Route.registerSiteRoutes(db: CoroutineDatabase) {
    val siteRepository = MongoRepository<Sites>(db.getCollection())
    val floorRepository = MongoFloorRepository(db.getCollection())

    route("/api/site") {
        routeRepository(siteRepository) {
            it.toDto()
        }

        post<SiteDto> {
            executeUseCase {
                CreateSiteUseCase.execute(it, siteRepository)
            }
        } describe {
            description = "Creates a new site"
            body {
                json {
                    schema<SiteDto>()
                }
            }
            200 response {
                description = "The created site"
                json {
                    schema<SiteDto>()
                }
            }
        }

        put<SiteDto> {
            executeUseCase {
                UpdateSiteUseCase.execute(it, siteRepository)
            }
        } describe {
            description = "Updates a site"
            body {
                json {
                    schema<SiteDto>()
                }
            }
            200 response {
                description = "The updated site"
                json {
                    schema<SiteDto>()
                }
            }
        }

        get("/{siteId}/floor") {
            val siteId = call.parameters["siteId"] ?: return@get call.respondText("Missing id", status = HttpStatusCode.BadRequest)

            if (!ObjectId.isValid(siteId)) {
                return@get call.respondText("Id $siteId is not a valid ObjectId", status = HttpStatusCode.BadRequest)
            }

            val floors = floorRepository.findBySiteId(ObjectId(siteId)).map() { it.toDto() }

            call.respond(floors)
        } describe {
            description = "Gets all floors for a site"
            "siteId" pathParameter {
                description = "The id of the site"
                schema(ObjectId().toString())
            }
            200 response {
                description = "The floors for the site"
                json {
                    schema<List<SiteDto>>()
                }
            }
        }
    }
}
