package replace.usecase.bookableentity

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import replace.dto.CreateBookableEntityDto
import replace.dto.toDto
import replace.model.BookableEntities
import replace.model.BookableEntity
import replace.model.BookableEntityType
import replace.model.BookableEntityTypes
import replace.model.Floor
import replace.model.Floors
import replace.model.Site
import java.util.UUID

class CreateBookableEntityUseCaseTest : FunSpec({
    context("happy path") {
        test("create a simple bookable entity") {
            Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")
            transaction {
                SchemaUtils.create(BookableEntityTypes, BookableEntities, Floors)
            }

            val entityType = transaction {
                BookableEntityType.new {
                    name = "desk"
                }
            }

            val site = transaction {
                Site.new {
                    name = "site 1"
                }
            }


            val floor = transaction {
                Floor.new {
                    name = "floor 1"
                    siteId = site.id
                }
            }

            val preInsertDto = CreateBookableEntityDto(
                name = "desk 1",
                posX = 10,
                posY = 20,
                floorId = floor.id.value,
                typeId = entityType.id.value,
            )

            val bookableEntityDto = CreateBookableEntityUseCase.execute(preInsertDto)

            val bookableEntityDtoGet = transaction {
                BookableEntity.findById(bookableEntityDto.id)?.toDto()
            }

            bookableEntityDto.id shouldNotBe null

            bookableEntityDto.id shouldBe bookableEntityDtoGet?.id
        }
    }
    context("sad path") {
        test("create a bookable entity with a name that already exists") {
            // TODO
        }
    }
})
