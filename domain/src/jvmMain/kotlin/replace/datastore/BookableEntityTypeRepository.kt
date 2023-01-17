package replace.datastore

import replace.model.BookableEntityTypes

interface BookableEntityTypeRepository : Repository<BookableEntityTypes> {
    suspend fun findByName(name: String): BookableEntityTypes?
}
