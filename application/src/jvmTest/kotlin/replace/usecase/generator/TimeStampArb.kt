package replace.usecase.generator

import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.instant
import io.kotest.property.arbitrary.map
import kotlinx.datetime.Instant
import kotlinx.datetime.toKotlinInstant

fun Arb.Companion.timeStamp(): Arb<Instant> = arbitrary {
    Arb.instant(
        minValue = java.time.Instant.ofEpochSecond(-3942000000),
        maxValue = java.time.Instant.ofEpochSecond(+3942000000),
    ).map { it.toKotlinInstant() }.bind()
}
