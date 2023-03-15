package replace.usecase.generator

import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.instant
import kotlinx.datetime.toKotlinInstant
import org.jetbrains.exposed.sql.transactions.transaction
import replace.model.Booking
import replace.model.User

fun ReplaceArb.booking(
    userArb: Arb<User> = ReplaceArb.user(),
): Arb<Booking> = arbitrary {
    val start = Arb.instant().bind().toKotlinInstant()
    val end = Arb.instant().bind().toKotlinInstant()
    val user = userArb.bind()
    transaction {
        Booking.new {
            this.start = start
            this.end = end
            this.user = user
        }
    }
}
