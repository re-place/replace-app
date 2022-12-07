package replace.plugin

import io.ktor.client.engine.*
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.ApplicationCallPipeline
import io.ktor.server.application.BaseApplicationPlugin
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.http.content.HttpStatusCodeContent
import io.ktor.server.http.content.files
import io.ktor.server.http.content.resolveResource
import io.ktor.server.http.content.resources
import io.ktor.server.http.content.static
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.request.acceptItems
import io.ktor.server.request.uri
import io.ktor.server.response.ApplicationSendPipeline
import io.ktor.server.response.respond
import io.ktor.server.response.respondFile
import io.ktor.server.routing.Route
import io.ktor.server.routing.routing
import io.ktor.util.AttributeKey
import io.ktor.util.pipeline.PipelineContext
import java.io.File
import java.io.FileNotFoundException
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Original source: https://github.com/lamba92/ktor-spa
 * Modified for authentication support
 *
 * The SPA configuration class.
 * @param configuration The object configured by the installation lambda.
 */
class SinglePageApplication(private val configuration: Configuration) {
    companion object Plugin : BaseApplicationPlugin<Application, Configuration, SinglePageApplication> {

        override val key = AttributeKey<SinglePageApplication>("SinglePageApplication")

        override fun install(
            pipeline: Application,
            configure: Configuration.() -> Unit,
        ): SinglePageApplication {
            val feature = SinglePageApplication(Configuration().apply(configure))
            pipeline.routing {
                feature.configuration.authConfiguration.apply {
                    if (this == null) {
                        routeStatic(feature.configuration)
                    } else {
                        authenticate(*configurations, optional = optional) {
                            routeStatic(feature.configuration)
                        }
                    }
                }
            }
            pipeline.intercept(ApplicationCallPipeline.Fallback) {
                if (call.response.status() == null) {
                    call.respond(HttpStatusCodeContent(HttpStatusCode.NotFound))
                    finish()
                }
            }
            pipeline.sendPipeline.intercept(ApplicationSendPipeline.Before) { message ->
                feature.intercept(this, message)
            }
            return feature
        }

        private fun Route.routeStatic(configuration: Configuration) {
            static(configuration.spaRoute) {
                if (configuration.useFiles) {
                    files(configuration.folderPath)
                } else {
                    resources(configuration.folderPath)
                }
            }
        }
    }

    private suspend fun intercept(
        pipelineContext: PipelineContext<Any, ApplicationCall>,
        message: Any,
    ) = with(pipelineContext) context@{

        val requestUrl = call.request.uri
        val regex = configuration.ignoreIfContains
        val stop by lazy {
            !(
                (regex == null || !requestUrl.contains(regex)) &&
                    (
                        requestUrl.startsWith(configuration.spaRoute) ||
                            requestUrl.startsWith("/${configuration.spaRoute}")
                        )
                )
        }
        val is404 by lazy {
            when (message) {
                is HttpStatusCode -> message == HttpStatusCode.NotFound
                is HttpStatusCodeContent -> message.status == HttpStatusCode.NotFound
                else -> false
            }
        }
        val acceptsHtml by lazy {
            call.request.acceptItems().any {
                ContentType.Text.Html.match(it.value)
            }
        }

        if (call.attributes.contains(StatusPages.key) || stop || !is404 || !acceptsHtml)
            return@context

        call.attributes.put(key, this@SinglePageApplication)

        if (configuration.useFiles) {
            val file = configuration.fullPath().toFile()
            if (!file.exists()) throw FileNotFoundException("${configuration.fullPath()} not found")
            call.respondFile(File(configuration.folderPath), configuration.defaultPage)
        } else {
            val indexPageApplication = call.resolveResource(configuration.fullPath().toString())
                ?: throw FileNotFoundException("${configuration.fullPath()} not found")
            call.respond(indexPageApplication)
        }
        finish()
    }

    data class Configuration(
        var spaRoute: String = "",
        var useFiles: Boolean = false,
        var folderPath: String = "",
        var defaultPage: String = "index.html",
        var ignoreIfContains: Regex? = null,
        var authConfiguration: AuthConfiguration? = null,
    ) {
        fun fullPath(): Path = Paths.get(folderPath, defaultPage)
    }

    class AuthConfiguration(
        vararg var configurations: String? = arrayOf(null),
        var optional: Boolean = false
    )
}
