package replace.usecase.bookableentity

import org.bson.types.ObjectId
import replace.datastore.Repository
import replace.dto.BookableEntityDto
import replace.dto.toDto
import replace.model.BookableEntity
import replace.model.BookableEntityType

object UpdateBookableEntityUseCase {
    suspend fun execute(
        bookableEntityDto: BookableEntityDto,
        bookableEntityRepository: Repository<BookableEntity>,
        bookableEntityTypeRepository: Repository<BookableEntityType>,
    ): BookableEntityDto {
        val id = ObjectId(bookableEntityDto.id)
        val name = bookableEntityDto.name
        val parentId = ObjectId(bookableEntityDto.id)
        val type = bookableEntityTypeRepository.findOneById(ObjectId(bookableEntityDto.typeId))
        checkNotNull(type) { "BookableEntityType ID not found" }

        val bookableEntity = BookableEntity(name, type, parentId)
        bookableEntity._id = id
        val updatedBookableEntity = bookableEntityRepository.updateOne(bookableEntity)
        checkNotNull(updatedBookableEntity)
        return updatedBookableEntity.toDto()
    }
}
