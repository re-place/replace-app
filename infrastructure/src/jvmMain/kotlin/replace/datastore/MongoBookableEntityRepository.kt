package replace.datastore

import org.bson.types.ObjectId
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq
import replace.model.BookableEntity
import replace.model.Floor

class MongoBookableEntityRepository(collection: CoroutineCollection<BookableEntity>) :
    MongoRepository<BookableEntity>(collection), BookableEntityRepository {
    override suspend fun forFloor(floorId: ObjectId): List<BookableEntity> = collection.find(BookableEntity::floorId eq floorId).toList()
}
