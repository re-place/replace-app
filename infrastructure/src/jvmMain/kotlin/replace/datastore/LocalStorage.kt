package replace.datastore

import java.io.File
import java.io.InputStream
import kotlin.io.path.Path
import kotlin.io.path.createDirectories

const val StoragePath = "storage/"

class LocalStorage : Storage {

    private fun ensureDirectoryExists(pathUri: String) {
        val path = Path(pathUri)
        path.createDirectories()
    }

    override suspend fun getFile(path: String): File = File("$StoragePath$path")

    override suspend fun saveFile(path: String, input: InputStream): File {
        val file = File("$StoragePath$path")

        ensureDirectoryExists(file.parent)

        input.use { its ->
            file.outputStream().buffered().use {
                its.copyTo(it)
            }
        }

        return file
    }

    override suspend fun deleteFile(path: String): Boolean {
        return File("$StoragePath$path").delete()
    }

    override suspend fun copyFile(from: String, to: String): File {
        val fromFile = File("$StoragePath$from")
        val toFile = File("$StoragePath$to")

        ensureDirectoryExists(toFile.parent)

        return fromFile.copyTo(toFile, overwrite = true)
    }
}
