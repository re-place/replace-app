package replace.usecase.bookableentity

import org.bson.types.ObjectId
import replace.datastore.BookableEntityRepository
import replace.datastore.BookableEntityTypeRepository
import replace.dto.BookableEntityDto
import replace.model.BookableEntityWithId

object CreateBookableEntityUseCase {
    suspend fun execute(
        bookableEntityDto: BookableEntityDto,
        bookableEntityRepository: BookableEntityRepository,
        bookableEntityTypeRepository: BookableEntityTypeRepository,
    ): BookableEntityWithId {
        val bookableEntityType = bookableEntityTypeRepository.findOneById(ObjectId(bookableEntityDto.typeId))
            ?: throw IllegalArgumentException("BookableEntityType with id ${bookableEntityDto.typeId} does not exist")

        val floorId = ObjectId(bookableEntityDto.floorId)
        val parentId = bookableEntityDto.parentId?.let { ObjectId(it) }
        val type = bookableEntityTypeRepository.findOneById(ObjectId(bookableEntityDto.typeId))
        checkNotNull(type) { "BookableEntityType ID not found" }

        val bookableEntity = bookableEntityRepository.createInstance(
            bookableEntityDto.name,
            bookableEntityType,
            floorId,
            parentId
        )

        val insertedBookableEntity = bookableEntityRepository.insertOne(bookableEntity)
        checkNotNull(insertedBookableEntity) { "Could not insert BookableEntity" }
        return insertedBookableEntity
    }
}
