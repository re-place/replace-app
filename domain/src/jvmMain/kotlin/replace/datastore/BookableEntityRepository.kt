package replace.datastore

import org.bson.types.ObjectId
import replace.model.BookableEntity

interface BookableEntityRepository : Repository<BookableEntity> {
    suspend fun forFloor(floorId: ObjectId): List<BookableEntity>
}
