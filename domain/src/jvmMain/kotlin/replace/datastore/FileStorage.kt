package replace.datastore

import java.io.InputStream

interface FileStorage {

    suspend fun exists(path: String): Boolean

    suspend fun saveFile(path: String, input: InputStream): Boolean

    suspend fun deleteFile(path: String): Boolean

    suspend fun copyFile(from: String, to: String): Boolean

    suspend fun readFile(path: String): InputStream

    suspend fun getFileSize(path: String): Long
}
