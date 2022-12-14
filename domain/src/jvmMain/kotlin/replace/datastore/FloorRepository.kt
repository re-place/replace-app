package replace.datastore

import org.bson.types.ObjectId
import replace.model.Floor

interface FloorRepository : Repository<Floor> {
    suspend fun findBySiteId(siteId: ObjectId): List<Floor>
}
