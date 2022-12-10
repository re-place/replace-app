package replace.usecase.bookableentity

import org.bson.types.ObjectId
import replace.datastore.BookableEntityRepository
import replace.datastore.Repository
import replace.dto.BookableEntityDto
import replace.dto.BookingDto
import replace.dto.toDto
import replace.model.BookableEntity
import replace.model.BookableEntityType
import replace.model.Booking

object CreateBookableEntityUseCase {
    suspend fun execute(
        bookableEntityDto: BookableEntityDto,
        bookableEntityRepository: Repository<BookableEntity>,
        bookableEntityTypeRepository: Repository<BookableEntityType>,
    ): BookableEntityDto {
        val name = bookableEntityDto.name
        val parentId = ObjectId(bookableEntityDto.id)
        val type = bookableEntityTypeRepository.findOneById(ObjectId(bookableEntityDto.typeId))
        checkNotNull(type) { "BookableEntityType ID not found" }

        val bookableEntity = BookableEntity(name, type, parentId)

        val insertedBookableEntity = bookableEntityRepository.insertOne(bookableEntity)
        checkNotNull(insertedBookableEntity) { "Could not insert BookableEntity" }
        return insertedBookableEntity.toDto()
    }
}
