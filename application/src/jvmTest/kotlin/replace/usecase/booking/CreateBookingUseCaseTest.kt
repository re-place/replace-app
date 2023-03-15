package replace.usecase.booking

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.email
import io.kotest.property.arbitrary.instant
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll
import io.kotest.property.resolution.default
import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.transactions.transaction
import replace.dto.CreateBookableEntityDto
import replace.dto.CreateBookingDto
import replace.model.BookableEntity
import replace.model.BookableEntityType
import replace.model.Floor
import replace.model.Site
import replace.model.User
import replace.usecase.prepareDatabase

class CreateBookingUseCaseTest : FunSpec({
    context("happy path") {
        test("create a simple booking with one bookable entity") {
            prepareDatabase()
            checkAll(
                Arb.string(1..100),
                Arb.string(1..100),
                Arb.string(1..100),
                Arb.string(1..100),
                Arb.int(0..100),
                Arb.int(0..100),
                Arb.email(),
                Arb.default(),
                Arb.default(),
            ) {
                    entityTypeName: String,
                    siteName: String,
                    floorName: String,
                    bookableEntityName: String,
                    bookableEntityPosX: Int,
                    bookableEntityTypePosY: Int,
                    userEmail: String,
                    bookingStart: Instant,
                    bookingEnd: Instant,
                ->
                val entityType = transaction { BookableEntityType.new { name = entityTypeName } }
                val site = transaction { Site.new { name = siteName } }
                val floor = transaction {
                    Floor.new {
                        name = floorName
                        siteId = site.id
                    }
                }
                Arb.email()
                val bookableEntity = transaction {
                    BookableEntity.new {
                        name = bookableEntityName
                        posX = bookableEntityPosX
                        posY = bookableEntityTypePosY
                        floorId = floor.id
                        typeId = entityType.id
                        index = 0
                    }
                }
                val user = transaction {
                    User.new {
                        email = userEmail
                    }
                }

                val preInsertDto = CreateBookingDto(
                    bookedEntityIds = listOf(bookableEntity.id.value),
                    start = bookingStart.toString(),
                    end = bookingEnd.toString(),
                )

                val fromUseCase = CreateBookingUseCase.execute(preInsertDto)
            }
        }
    }
})
