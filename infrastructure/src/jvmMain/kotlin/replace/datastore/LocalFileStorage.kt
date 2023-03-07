package replace.datastore

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream
import kotlin.io.path.Path
import kotlin.io.path.createDirectories

class LocalFileStorage : FileStorage {

    private suspend fun ensureDirectoryExists(pathUri: String) = withContext(Dispatchers.IO) {
        val path = Path(pathUri)
        path.createDirectories()
    }

    override suspend fun exists(path: String): Boolean = withContext(Dispatchers.IO) {
        File("$STORAGE_PATH$path").exists()
    }

    override suspend fun saveFile(path: String, input: InputStream): Boolean = withContext(Dispatchers.IO) {
        val file = File("$STORAGE_PATH$path")

        ensureDirectoryExists(file.parent)

        input.use { its ->
            file.outputStream().buffered().use {
                its.copyTo(it)
            }
        }

        file.exists()
    }

    override suspend fun deleteFile(path: String): Boolean = withContext(Dispatchers.IO) {
        File("$STORAGE_PATH$path").delete()
    }

    override suspend fun copyFile(from: String, to: String): Boolean = withContext(Dispatchers.IO) {
        val fromFile = File("$STORAGE_PATH$from")
        val toFile = File("$STORAGE_PATH$to")

        ensureDirectoryExists(toFile.parent)

        fromFile.copyTo(toFile, overwrite = true).exists()
    }

    override suspend fun readFile(path: String): InputStream = withContext(Dispatchers.IO) {
        File("$STORAGE_PATH$path").inputStream()
    }

    override suspend fun getFileSize(path: String): Long = withContext(Dispatchers.IO) {
        File("$STORAGE_PATH$path").length()
    }

    private companion object {
        const val STORAGE_PATH = "storage/"
    }
}
