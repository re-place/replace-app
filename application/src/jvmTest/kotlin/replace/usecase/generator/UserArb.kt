package replace.usecase.generator

import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.codepoints
import io.kotest.property.arbitrary.email
import io.kotest.property.arbitrary.string
import org.jetbrains.exposed.sql.transactions.transaction
import replace.model.User

fun ReplaceArb.user(): Arb<User> = arbitrary {
    val email = Arb.email().bind()
    val firstname = Arb.string(1..100, codepoints = Arb.codepoints()).bind()
    val lastname = Arb.string(1..100, codepoints = Arb.codepoints()).bind()
    transaction {
        User.new {
            this.email = email
            this.firstname = firstname
            this.lastname = lastname
        }
    }
}
