package replace.usecase.bookableentity

import org.jetbrains.exposed.dao.id.EntityID
import replace.dto.BookableEntityDto
import replace.dto.CreateBookableEntityDto
import replace.dto.toDto
import replace.model.BookableEntities
import replace.model.BookableEntity
import replace.model.BookableEntityTypes
import replace.model.Floors

object CreateBookableEntityUseCase {
    fun execute(
        bookableEntityDto: CreateBookableEntityDto,
    ): BookableEntityDto {
        val bookableEntity = BookableEntity.new {
            name = bookableEntityDto.name
            floorId = EntityID(bookableEntityDto.floorId, Floors)
            parentId = bookableEntityDto.parentId?.let { EntityID(it, BookableEntities) }
            typeId = bookableEntityDto.typeId?.let { EntityID(it, BookableEntityTypes) }
        }

        return bookableEntity.toDto()
    }
}
