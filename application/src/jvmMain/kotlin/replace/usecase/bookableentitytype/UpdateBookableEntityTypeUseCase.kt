package replace.usecase.bookableentitytype

import org.bson.types.ObjectId
import replace.datastore.BookableEntityTypeRepository
import replace.dto.BookableEntityTypeDto
import replace.dto.toDto
import replace.dto.toModel

object UpdateBookableEntityTypeUseCase {
    suspend fun execute(
        bookableEntityTypeDto: BookableEntityTypeDto,
        bookableEntityTypeRepository: BookableEntityTypeRepository,
    ): BookableEntityTypeDto {
        val bookableEntityTypeId = ObjectId(bookableEntityTypeDto.id)

        val updatedBookableEntityType = bookableEntityTypeRepository.updateOne(bookableEntityTypeId, bookableEntityTypeDto.toModel())
        checkNotNull(updatedBookableEntityType) { "Could not update BookableEntityType" }
        return updatedBookableEntityType.toDto()
    }
}
