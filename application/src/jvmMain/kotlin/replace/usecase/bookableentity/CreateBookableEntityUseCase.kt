package replace.usecase.bookableentity

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import replace.dto.BookableEntityDto
import replace.dto.CreateBookableEntityDto
import replace.dto.toDto
import replace.model.BookableEntities
import replace.model.BookableEntity
import replace.model.BookableEntityTypes
import replace.model.Floors

object CreateBookableEntityUseCase {
    suspend fun execute(
        bookableEntityDto: CreateBookableEntityDto,
    ): BookableEntityDto {
        return newSuspendedTransaction {
            val bookableEntity = BookableEntity.new {
                name = bookableEntityDto.name
                floorId = EntityID(bookableEntityDto.floorId, Floors)
                parentId = bookableEntityDto.parentId?.let { EntityID(it, BookableEntities) }
                typeId = bookableEntityDto.typeId?.let { EntityID(it, BookableEntityTypes) }
                posX = bookableEntityDto.posX
                posY = bookableEntityDto.posY
                index = bookableEntityDto.index
            }

            bookableEntity.toDto()
        }
    }
}
