package replace.usecase.bookableentity

import org.bson.types.ObjectId
import replace.datastore.Repository
import replace.dto.BookableEntityDto
import replace.model.BookableEntity

object DeleteBookableEntityUseCase {
    suspend fun execute(
        bookableEntityDto: BookableEntityDto,
        bookableEntityRepository: Repository<BookableEntity>,
    ): BookableEntityDto {
        val id = ObjectId(bookableEntityDto.id)
        val deleted = bookableEntityRepository.deleteOne(id)
        check(deleted) { "Could not delete BookableEntity" }

        return bookableEntityDto
    }
}
