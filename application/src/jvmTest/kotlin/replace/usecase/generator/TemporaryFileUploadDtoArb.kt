package replace.usecase.generator

import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.byte
import io.kotest.property.arbitrary.byteArray
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.string
import org.jetbrains.exposed.sql.transactions.transaction
import replace.datastore.DBFile
import replace.model.TemporaryFile

data class DBFileTuple<T>(
    val dbFile: DBFile,
    val file: T,
)

fun ReplaceArb.temporaryFile(): Arb<DBFileTuple<TemporaryFile>> = arbitrary {
    val name = Arb.string(1..100).bind()
    val path = Arb.string(1..1000).bind()
    val extension = Arb.string(1..10).bind()
    val mime = Arb.string(1..100).bind()
    val sizeInBytes = Arb.long(1L..10_000_000L).bind()
    val createdAt = Arb.timeStamp().bind()
    val data = Arb.byteArray(Arb.int(1_000..100_000), Arb.byte()).bind()
    transaction {
        DBFileTuple(
            DBFile.new(path) {
                this.data = data
            },
            TemporaryFile.new {
                this.name = name
                this.path = path
                this.extension = extension
                this.mime = mime
                this.sizeInBytes = sizeInBytes
                this.createdAt = createdAt
            },
        )
    }
}
