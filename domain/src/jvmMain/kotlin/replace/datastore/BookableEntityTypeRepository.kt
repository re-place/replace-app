package replace.datastore

import org.bson.types.ObjectId
import replace.model.BookableEntityType
import replace.model.BookableEntityTypeWithId

interface BookableEntityTypeRepository : Repository<BookableEntityType, BookableEntityTypeWithId> {
    fun createInstance(
        id: ObjectId?,
        name: String,
    ): BookableEntityType

    suspend fun findByName(name: String): BookableEntityType?
}
