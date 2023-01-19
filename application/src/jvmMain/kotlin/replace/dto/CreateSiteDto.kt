package replace.dto

import kotlin.reflect.KProperty1
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.transactions.transaction
import replace.model.Site

@Serializable
class CreateSiteDto(
    val name: String,
)
