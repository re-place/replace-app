package replace.usecase.generator

import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.email
import io.kotest.property.arbitrary.string
import io.kotest.property.arbitrary.uuid
import org.jetbrains.exposed.sql.transactions.transaction
import replace.dto.UserDto
import replace.model.User

fun ReplaceArb.user(): Arb<User> = arbitrary {
    val email = Arb.email().bind()
    val firstname = Arb.string(1..100).bind()
    val lastname = Arb.string(1..100).bind()
    transaction {
        User.new {
            this.email = email
            this.firstname = firstname
            this.lastname = lastname
        }
    }
}

fun ReplaceArb.userDto(): Arb<UserDto> = arbitrary {
    val id = Arb.uuid().bind().toString()
    val email = Arb.email().bind()
    val firstname = Arb.string(1..100).bind()
    val lastname = Arb.string(1..100).bind()
    UserDto(id, email, firstname, lastname)
}
