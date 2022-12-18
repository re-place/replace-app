package replace.datastore

import org.bson.types.ObjectId
import replace.model.Floor
import replace.model.FloorWithId

interface FloorRepository : Repository<Floor, FloorWithId> {
    fun createInstance(
        id: ObjectId?,
        name: String,
        siteId: ObjectId,
    ): Floor
    suspend fun findBySiteId(siteId: ObjectId): List<Floor>
}
