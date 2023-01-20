package replace.usecase.bookableentity

import org.jetbrains.exposed.sql.transactions.transaction
import replace.dto.BookableEntityDto
import replace.dto.UpdateBookableEntityPositionDto
import replace.dto.toDto
import replace.model.BookableEntity

object UpdateBookableEntityPositionUseCase {
    suspend fun execute(
        bookableEntityDto: UpdateBookableEntityPositionDto,
    ): BookableEntityDto {

        return transaction {
            val bookableEntity = BookableEntity.findById(bookableEntityDto.id)

            checkNotNull(bookableEntity) { "BookableEntity with id ${bookableEntityDto.id} not found" }

            bookableEntity.posX = bookableEntityDto.posX
            bookableEntity.posY = bookableEntityDto.posY

            bookableEntity.toDto()
        }
    }
}
