package replace.http.controller

import io.ktor.resources.Resource
import kotlinx.serialization.Serializable

object Routing {
    @Serializable
    @Resource("{id}")
    data class ById(val id: String) {
        @Serializable
        @Resource("edit")
        data class Edit(val parent: ById)
    }
}
