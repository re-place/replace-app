package replace.usecase.bookableentitytype

import org.bson.types.ObjectId
import replace.datastore.BookableEntityTypeRepository
import replace.dto.BookableEntityTypeDto
import replace.dto.toDto
import replace.model.BookableEntityType

object UpdateBookableEntityTypeUseCase {
    suspend fun execute(
        bookableEntityTypeDto: BookableEntityTypeDto,
        bookableEntityTypeRepository: BookableEntityTypeRepository,
    ): BookableEntityTypeDto {
        val name = bookableEntityTypeDto.name
        val id = ObjectId(bookableEntityTypeDto.id)

        val bookableEntityType = BookableEntityType(name)
        bookableEntityType._id = id

        val updatedBookableEntityType = bookableEntityTypeRepository.updateOne(bookableEntityType)
        checkNotNull(updatedBookableEntityType) { "Could not update BookableEntityType" }
        return updatedBookableEntityType.toDto()
    }
}
