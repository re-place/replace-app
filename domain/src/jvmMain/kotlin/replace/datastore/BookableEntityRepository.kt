package replace.datastore

import org.bson.types.ObjectId
import replace.model.BookableEntity
import replace.model.BookableEntityType
import replace.model.BookableEntityWithId

interface BookableEntityRepository : Repository<BookableEntity, BookableEntityWithId> {
    fun createInstance(
        name: String,
        type: BookableEntityType,
        floorId: ObjectId,
        parentId: ObjectId?,
    ): BookableEntity
}
