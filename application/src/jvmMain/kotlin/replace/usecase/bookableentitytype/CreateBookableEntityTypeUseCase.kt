package replace.usecase.bookableentitytype

import replace.datastore.BookableEntityTypeRepository
import replace.dto.BookableEntityTypeDto
import replace.dto.toDto
import replace.model.BookableEntityType

object CreateBookableEntityTypeUseCase {

    suspend fun execute(
        bookableEntityTypeDto: BookableEntityTypeDto,
        bookableEntityTypeRepository: BookableEntityTypeRepository,
    ): BookableEntityTypeDto {
        val foundType = bookableEntityTypeRepository.findByName(bookableEntityTypeDto.name)
        if (foundType != null) {
            throw IllegalStateException("BookableEntityType with given name already exists")
        }
        val type = BookableEntityType(bookableEntityTypeDto.name)
        val insertedBookableEntityType = bookableEntityTypeRepository.insertOne(type)
        checkNotNull(insertedBookableEntityType) { "Could not insert BookableEntityType" }
        return insertedBookableEntityType.toDto()
    }
}
