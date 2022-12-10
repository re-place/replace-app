package replace.datastore

import replace.model.BookableEntityType

interface BookableEntityTypeRepository : Repository<BookableEntityType> {
    suspend fun findByName(name: String): BookableEntityType?
}
