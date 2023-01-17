package replace.datastore

import replace.model.BookableEntities

interface BookableEntityRepository : Repository<BookableEntities> {
    suspend fun forFloor(floorId: String): List<BookableEntities>
}
