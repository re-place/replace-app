package replace.datastore

import org.bson.types.ObjectId
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq
import replace.model.Floors

class MongoFloorRepository(collection: CoroutineCollection<Floors>) :
    MongoRepository<Floors>(collection), FloorRepository {
    override suspend fun findBySiteId(siteId: ObjectId): List<Floors> =
        collection.find(Floors::siteId eq siteId).toList()
}
