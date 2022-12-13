package replace.model

import kotlinx.serialization.Serializable

@Serializable
<<<<<<<< HEAD:domain/src/jvmMain/kotlin/replace/model/Site.kt
data class Site(
========
data class OfficeBuilding(
>>>>>>>> 3636fe3 (rename office building):domain/src/jvmMain/kotlin/replace/model/OfficeBuilding.kt
    val name: String,
) : ObjectWithId()
