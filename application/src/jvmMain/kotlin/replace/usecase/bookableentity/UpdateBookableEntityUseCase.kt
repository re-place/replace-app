package replace.usecase.bookableentity

import org.bson.types.ObjectId
import replace.datastore.Repository
import replace.dto.BookableEntityDto
import replace.dto.toDto
import replace.dto.toModel
import replace.model.BookableEntity

object UpdateBookableEntityUseCase {
    suspend fun execute(
        bookableEntityDto: BookableEntityDto,
        bookableEntityRepository: Repository<BookableEntity>,
    ): BookableEntityDto {
        val bookableEntityId = ObjectId(bookableEntityDto.id)

        val updatedBookableEntity = bookableEntityRepository.updateOne(bookableEntityId, bookableEntityDto.toModel())
        checkNotNull(updatedBookableEntity)
        return updatedBookableEntity.toDto()
    }
}
