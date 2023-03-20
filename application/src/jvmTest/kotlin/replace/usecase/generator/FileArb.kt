package replace.usecase.generator

import io.kotest.common.runBlocking
import io.kotest.property.Arb
import io.kotest.property.arbitrary.Codepoint
import io.kotest.property.arbitrary.alphanumeric
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.byte
import io.kotest.property.arbitrary.byteArray
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.string
import org.jetbrains.exposed.sql.transactions.transaction
import replace.datastore.FileStorage
import replace.model.File
import replace.model.TemporaryFile

data class DBFileTuple<T>(
    val data: ByteArray,
    val file: T,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DBFileTuple<*>

        if (!data.contentEquals(other.data)) return false
        if (file != other.file) return false

        return true
    }

    override fun hashCode(): Int {
        var result = data.contentHashCode()
        result = 31 * result + (file?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "DBFileTuple(data.size=${data.size}, data.hashcode=${data.hashCode()}, file=$file)"
    }
}

fun ReplaceArb.file(
    fileStorage: FileStorage,
): Arb<DBFileTuple<File>> = arbitrary {
    val name = Arb.string(1..100).bind()
    val path = Arb.string(1..1000).bind()
    val extension = Arb.string(1..10).bind()
    val mime = Arb.string(1..100).bind()
    val sizeInBytes = Arb.long(1L..10_000_000L).bind()
    val data = Arb.byteArray(Arb.int(1_000..100_000), Arb.byte()).bind()
    runBlocking {
        fileStorage.saveFile(path, data.inputStream())
    }
    transaction {
        DBFileTuple(
            data,
            File.new {
                this.name = name
                this.path = path
                this.extension = extension
                this.mime = mime
                this.sizeInBytes = sizeInBytes
            },
        )
    }
}

fun ReplaceArb.temporaryFile(
    fileStorage: FileStorage,
): Arb<DBFileTuple<TemporaryFile>> = arbitrary {
    val name = Arb.string(1..100, codepoints = Codepoint.alphanumeric()).bind()
    val path = Arb.string(1..1000, codepoints = Codepoint.alphanumeric()).bind()
    val extension = Arb.string(1..10, codepoints = Codepoint.alphanumeric()).bind()
    val mime = Arb.string(1..100).bind()
    val sizeInBytes = Arb.long(1L..10_000_000L).bind()
    val createdAt = Arb.timeStamp().bind()
    val data = Arb.byteArray(Arb.int(1_000..100_000), Arb.byte()).bind()
    runBlocking {
        fileStorage.saveFile(path, data.inputStream())
    }
    transaction {
        DBFileTuple(
            data,
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
