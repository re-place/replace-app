package replace.usecase.bookableentitytype

import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import replace.dto.BookableEntityTypeDto
import replace.dto.CreateBookableEntityTypeDto
import replace.dto.toDto
import replace.model.BookableEntityType

object CreateBookableEntityTypeUseCase {

    suspend fun execute(
        createBookableEntityTypeDto: CreateBookableEntityTypeDto,
    ): BookableEntityTypeDto {
        return newSuspendedTransaction {
            val bookableEntityType = BookableEntityType.new {
                name = createBookableEntityTypeDto.name
            }

            bookableEntityType.toDto()
        }
    }
}
