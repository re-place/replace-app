package replace.datastore

import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq
import replace.model.BookableEntityTypes

class MongoBookableEntityTypeRepository(collection: CoroutineCollection<BookableEntityTypes>) :
    MongoRepository<BookableEntityTypes>(collection), BookableEntityTypeRepository {
    override suspend fun findByName(name: String): BookableEntityTypes? =
        collection.findOne(BookableEntityTypes::name eq name)
}
