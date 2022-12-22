package replace.datastore

import java.io.File
import java.io.InputStream

interface Storage {
    suspend fun getFile(path: String): File

    suspend fun saveFile(path: String, input: InputStream): File

    suspend fun deleteFile(path: String): Boolean

    suspend fun copyFile(from: String, to: String): File
}
