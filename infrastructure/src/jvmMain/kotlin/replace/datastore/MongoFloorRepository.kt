package replace.datastore

import org.bson.types.ObjectId
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq
import replace.model.Floor

class MongoFloorRepository(collection: CoroutineCollection<Floor>) :
    MongoRepository<Floor>(collection), FloorRepository {
    override suspend fun findByOfficeId(officeId: ObjectId): List<Floor> =
        collection.find(Floor::locationId eq officeId).toList()
}
