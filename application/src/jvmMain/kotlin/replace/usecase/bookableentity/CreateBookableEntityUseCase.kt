package replace.usecase.bookableentity

import org.bson.types.ObjectId
import replace.datastore.Repository
import replace.dto.BookableEntityDto
import replace.dto.toDto
import replace.dto.toModel
import replace.model.BookableEntity
import replace.model.BookableEntityType

object CreateBookableEntityUseCase {
    suspend fun execute(
        bookableEntityDto: BookableEntityDto,
        bookableEntityRepository: Repository<BookableEntity>,
        bookableEntityTypeRepository: Repository<BookableEntityType>,
    ): BookableEntityDto {

        val bookableEntityTypeId = bookableEntityDto.type?.id

        if (bookableEntityTypeId !== null) {
            val bookableEntityType = bookableEntityTypeRepository.findOneById(ObjectId(bookableEntityDto.type.id))
            checkNotNull(bookableEntityType) { " Bookable Entity Type $bookableEntityTypeId does not exists" }
        }

        val insertedBookableEntity = bookableEntityRepository.insertOne(bookableEntityDto.toModel())
        checkNotNull(insertedBookableEntity) { "Could not insert BookableEntity" }
        return insertedBookableEntity.toDto()
    }
}
