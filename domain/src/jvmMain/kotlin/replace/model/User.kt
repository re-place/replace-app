package replace.model

data class User(
    val username: String,
    val password: String,
    val firstName: String,
    val lastName: String,
) : ObjectWithId()
