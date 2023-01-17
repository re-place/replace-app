package replace.datastore

import org.bson.types.ObjectId
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq
import replace.model.BookableEntities

class MongoBookableEntityRepository(collection: CoroutineCollection<BookableEntities>) :
    MongoRepository<BookableEntities>(collection), BookableEntityRepository {
    override suspend fun forFloor(floorId: ObjectId): List<BookableEntities> = collection.find(BookableEntities::floorId eq floorId).toList()
}
