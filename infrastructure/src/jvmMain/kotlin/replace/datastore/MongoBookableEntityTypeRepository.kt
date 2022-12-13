package replace.datastore

import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq
import replace.model.BookableEntityType

class MongoBookableEntityTypeRepository(collection: CoroutineCollection<BookableEntityType>) :
    MongoRepository<BookableEntityType>(collection), BookableEntityTypeRepository {
    override suspend fun findByName(name: String): BookableEntityType? =
        collection.findOne(BookableEntityType::name eq name)
}
