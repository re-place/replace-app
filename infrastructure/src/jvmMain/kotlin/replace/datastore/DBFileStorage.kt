package replace.datastore

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.io.InputStream

class DBFileStorage : FileStorage {

    override suspend fun exists(path: String): Boolean = newSuspendedTransaction {
        DBFile.findById(path) != null
    }

    override suspend fun saveFile(path: String, input: InputStream): Boolean = newSuspendedTransaction {
        DBFile.new(path) {
            data = input.readBytes()
        }

        true
    }

    override suspend fun deleteFile(path: String): Boolean = newSuspendedTransaction {
        DBFiles.deleteWhere { id eq path }
        true
    }

    override suspend fun copyFile(from: String, to: String): Boolean = newSuspendedTransaction {
        val oldFile = DBFile.findById(from) ?: return@newSuspendedTransaction false

        DBFile.new(to) {
            data = oldFile.data
        }

        true
    }

    override suspend fun readFile(path: String): InputStream {
        return newSuspendedTransaction {
            DBFile.findById(path)?.data?.inputStream()
                ?: InputStream.nullInputStream()
        }
    }

    override suspend fun getFileSize(path: String): Long = newSuspendedTransaction {
        val file = DBFile.findById(path) ?: return@newSuspendedTransaction 0L

        file.data.size.toLong()
    }
}
