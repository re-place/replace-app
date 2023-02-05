package replace.usecase.bookableentitytype

import org.jetbrains.exposed.sql.transactions.transaction
import replace.dto.BookableEntityTypeDto
import replace.dto.CreateBookableEntityTypeDto
import replace.dto.toDto
import replace.model.BookableEntityType

object CreateBookableEntityTypeUseCase {

    fun execute(
        createBookableEntityTypeDto: CreateBookableEntityTypeDto,
    ): BookableEntityTypeDto {

        return transaction {

            val bookableEntityType = BookableEntityType.new {
                name = createBookableEntityTypeDto.name
            }

            bookableEntityType.toDto()
        }
    }
}
