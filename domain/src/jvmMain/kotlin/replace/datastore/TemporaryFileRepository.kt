package replace.datastore

import replace.model.TemporaryFiles
import java.time.LocalDateTime

interface TemporaryFileRepository : Repository<TemporaryFiles> {
    suspend fun findOlderThan(datetime: LocalDateTime): List<TemporaryFiles>
}
