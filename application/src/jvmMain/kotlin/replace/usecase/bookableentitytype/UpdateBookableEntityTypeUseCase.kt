package replace.usecase.bookableentitytype

import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import replace.dto.BookableEntityTypeDto
import replace.dto.UpdateBookableEntityTypeDto
import replace.dto.toDto
import replace.model.BookableEntityType

object UpdateBookableEntityTypeUseCase {
    suspend fun execute(
        updateBookableEntityTypeDto: UpdateBookableEntityTypeDto,
    ): BookableEntityTypeDto {
        return newSuspendedTransaction {
            val bookableEntityType = BookableEntityType.findById(updateBookableEntityTypeDto.id)

            checkNotNull(bookableEntityType) { "BookableEntityType with id ${updateBookableEntityTypeDto.id} not found" }

            bookableEntityType.name = updateBookableEntityTypeDto.name

            bookableEntityType.toDto()
        }
    }
}
