package replace.usecase.bookableentitytype

import replace.dto.BookableEntityTypeDto
import replace.dto.UpdateBookableEntityTypeDto
import replace.dto.toDto
import replace.model.BookableEntityType

object UpdateBookableEntityTypeUseCase {
    suspend fun execute(
        updateBookableEntityTypeDto: UpdateBookableEntityTypeDto,
    ): BookableEntityTypeDto {

        val bookableEntityType = BookableEntityType.findById(updateBookableEntityTypeDto.id)

        checkNotNull(bookableEntityType) { "BookableEntityType with id ${updateBookableEntityTypeDto.id} not found" }

        bookableEntityType.name = updateBookableEntityTypeDto.name

        bookableEntityType.refresh()

        return bookableEntityType.toDto()
    }
}
