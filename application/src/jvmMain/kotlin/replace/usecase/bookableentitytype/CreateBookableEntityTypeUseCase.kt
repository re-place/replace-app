package replace.usecase.bookableentitytype

import replace.dto.BookableEntityTypeDto
import replace.dto.CreateBookableEntityTypeDto
import replace.dto.toDto
import replace.model.BookableEntityType

object CreateBookableEntityTypeUseCase {

    fun execute(
        createBookableEntityTypeDto: CreateBookableEntityTypeDto,
    ): BookableEntityTypeDto {

        val bookableEntityType = BookableEntityType.new {
            name = createBookableEntityTypeDto.name
        }

        return bookableEntityType.toDto()
    }
}
