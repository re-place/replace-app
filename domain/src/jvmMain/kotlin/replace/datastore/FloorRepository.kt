package replace.datastore

import replace.model.Floors

interface FloorRepository : Repository<Floors> {
    suspend fun findBySiteId(siteId: String): List<Floors>
}
