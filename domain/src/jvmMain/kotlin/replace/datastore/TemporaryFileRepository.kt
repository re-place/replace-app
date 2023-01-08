package replace.datastore

import replace.model.TemporaryFile
import java.time.LocalDateTime

interface TemporaryFileRepository : Repository<TemporaryFile> {
    suspend fun findOlderThan(datetime: LocalDateTime): List<TemporaryFile>
}
