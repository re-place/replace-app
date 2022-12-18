package replace.usecase.bookableentity

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.bson.types.ObjectId
import replace.dto.BookableEntityDto
import replace.model.BookableEntity
import replace.model.BookableEntityType
import replace.usecase.datastore.MockRepository

class CreateBookableEntityUseCaseTest : FunSpec({
    context("happy path") {
        test("create a simple bookable entity") {
            val bookableEntityRepository = MockRepository<BookableEntity>()
            val bookableEntityTypeRepository = MockRepository<BookableEntityType>()
            val entityType = bookableEntityTypeRepository.insertOne(BookableEntityType("desk"))

            val preInsertDto = BookableEntityDto(
                name = "desk 1",
                typeId = checkNotNull(entityType.id) { "EntityType id is null" }.toString(),
            )

            val bookableEntityDto = CreateBookableEntityUseCase.execute(
                preInsertDto,
                bookableEntityRepository,
                bookableEntityTypeRepository,
            )

            bookableEntityDto.id shouldNotBe null
            ObjectId.isValid(bookableEntityDto.id) shouldBe true

            bookableEntityDto.name shouldBe preInsertDto.name
            bookableEntityDto.typeId shouldBe preInsertDto.typeId
        }
    }
    context("sad path") {
        test("create a bookable entity with a name that already exists") {
            // TODO
        }
    }
})
