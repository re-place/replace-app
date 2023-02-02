package replace.usecase.bookableentity

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.transactions.transaction
import replace.dto.BookableEntityDto
import replace.dto.UpdateBookableEntityDto
import replace.dto.toDto
import replace.model.BookableEntities
import replace.model.BookableEntity
import replace.model.Floors

object UpdateBookableEntityUseCase {
    suspend fun execute(
        bookableEntityDto: UpdateBookableEntityDto,
    ): BookableEntityDto {

        return transaction {
            val bookableEntity = BookableEntity.findById(bookableEntityDto.id)

            checkNotNull(bookableEntity) { "BookableEntity with id ${bookableEntityDto.id} not found" }

            bookableEntity.name = bookableEntityDto.name
            bookableEntity.floorId = EntityID(bookableEntityDto.floorId, Floors)
            bookableEntity.parentId = bookableEntityDto.parentId?.let { EntityID(it, BookableEntities) }
            bookableEntity.typeId = bookableEntityDto.typeId?.let { EntityID(it, BookableEntities) }
            bookableEntity.posX = bookableEntityDto.posX
            bookableEntity.posY = bookableEntityDto.posY

            bookableEntity.toDto()
        }
    }
}
