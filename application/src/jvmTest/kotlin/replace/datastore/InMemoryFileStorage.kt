package replace.datastore

import java.io.InputStream

class InMemoryFileStorage : FileStorage {

    private val backing: MutableMap<String, ByteArray> = mutableMapOf()

    override suspend fun exists(path: String): Boolean {
        return backing.containsKey(path)
    }

    override suspend fun saveFile(path: String, input: InputStream): Boolean {
        backing[path] = input.readBytes()
        return true
    }

    override suspend fun deleteFile(path: String): Boolean {
        backing.remove(path)
        return true
    }

    override suspend fun copyFile(from: String, to: String): Boolean {
        backing[to] = backing[from] ?: return false
        return true
    }

    override suspend fun readFile(path: String): InputStream {
        return backing[path]?.inputStream() ?: InputStream.nullInputStream()
    }

    override suspend fun getFileSize(path: String): Long {
        return backing[path]?.size?.toLong() ?: 0L
    }
}
