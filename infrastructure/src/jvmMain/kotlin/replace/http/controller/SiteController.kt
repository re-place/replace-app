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
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import replace.dto.CreateSiteDto
import replace.dto.SiteDto
import replace.dto.UpdateSiteDto
import replace.dto.toDto
import replace.http.routeRepository
import replace.model.Floor
import replace.model.Floors
import replace.model.Site
import replace.usecase.site.CreateSiteUseCase
import replace.usecase.site.UpdateSiteUseCase

fun Route.registerSiteRoutes() {
    route("/api/site") {
        routeRepository(Site.Companion) {
            it.toDto()
        }

        post<CreateSiteDto> {
            executeUseCase {
                CreateSiteUseCase.execute(it)
            }
        } describe {
            description = "Creates a new site"
            body {
                json {
                    schema<CreateSiteDto>()
                }
            }
            200 response {
                description = "The created site"
                json {
                    schema<SiteDto>()
                }
            }
        }

        put<UpdateSiteDto> {
            executeUseCase {
                UpdateSiteUseCase.execute(it)
            }
        } describe {
            description = "Updates a site"
            body {
                json {
                    schema<UpdateSiteDto>()
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

            val floors = transaction { Floor.find { Floors.site_id eq siteId }.toList().map { it.toDto() } }

            call.respond(floors)
        } describe {
            description = "Gets all floors for a site"
            "siteId" pathParameter {
                description = "The id of the site"
                schema(String)
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
