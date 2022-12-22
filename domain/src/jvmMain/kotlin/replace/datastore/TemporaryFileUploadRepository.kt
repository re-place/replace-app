package replace.datastore

import replace.model.TemporaryFileUpload
import java.time.LocalDateTime

interface TemporaryFileUploadRepository : Repository<TemporaryFileUpload> {
    suspend fun findOlderThan(datetime: LocalDateTime): List<TemporaryFileUpload>
}
