package replace.usecase.bookableentity

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import replace.dto.UpdateBookableEntityOrderDto
import replace.model.BookableEntities
import replace.model.BookableEntity
import replace.model.Floors

object UpdateBookableEntityOrderUseCase {
    suspend fun execute(
        bookableEntityOrderDto: UpdateBookableEntityOrderDto,
    ) {
        return transaction {
            BookableEntity.find {
                BookableEntities.id inList bookableEntityOrderDto.bookableEntityIds and
                    (BookableEntities.floor_id eq EntityID(bookableEntityOrderDto.floorId, Floors))
            }
                .forEach { bookableEntity ->
                    bookableEntity.index = bookableEntityOrderDto.bookableEntityIds.indexOf(bookableEntity.id.value)
                }
        }
    }
}
