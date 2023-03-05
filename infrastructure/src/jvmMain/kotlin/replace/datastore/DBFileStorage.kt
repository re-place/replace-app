package replace.datastore

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.InputStream

class DBFileStorage : FileStorage {

    override suspend fun exists(path: String): Boolean {
        return transaction {
            return@transaction DBFile.findById(path) != null
        }
    }

    override suspend fun saveFile(path: String, input: InputStream): Boolean {
        return transaction {
            DBFile.new(path) {
                data = input.readBytes()
            }

            return@transaction true
        }
    }

    override suspend fun deleteFile(path: String): Boolean {
        return transaction {
            DBFiles.deleteWhere { DBFiles.id eq path }

            return@transaction true
        }
    }

    override suspend fun copyFile(from: String, to: String): Boolean {
        return transaction {

            val oldFile = DBFile.findById(from) ?: return@transaction false

            DBFile.new(to) {
                data = oldFile.data
            }

            return@transaction true
        }
    }

    override suspend fun readFile(path: String): InputStream {
        return transaction {
            val file = DBFile.findById(path) ?: return@transaction InputStream.nullInputStream()

            return@transaction file.data.inputStream()
        }
    }

    override suspend fun getFileSize(path: String): Long {
        return transaction {
            val file = DBFile.findById(path) ?: return@transaction 0L

            return@transaction file.data.size.toLong()
        }
    }
}
