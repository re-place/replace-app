package replace.usecase.booking

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.property.checkAll
import org.jetbrains.exposed.sql.transactions.transaction
import replace.model.BookableEntity
import replace.usecase.generator.ReplaceArb
import replace.usecase.generator.bookingCreateDto
import replace.usecase.generator.user
import replace.usecase.useDatabase
import java.util.UUID

class CreateBookingUseCaseTest : FunSpec({
    context("happy path") {
        test("create a simple booking with one bookable entity") {
            useDatabase {
                checkAll(ReplaceArb.bookingCreateDto(), ReplaceArb.user()) { dto, user ->
                    val fromUseCase = CreateBookingUseCase.execute(dto, user.id.toString())

                    fromUseCase.id shouldNotBe null
                    shouldNotThrowAny { UUID.fromString(fromUseCase.id) }
                    fromUseCase.userId shouldBe user.id.toString()
                    fromUseCase.bookedEntities shouldNotBe null
                    fromUseCase.bookedEntities!!.forEach {
                        it.id shouldNotBe null
                        shouldNotThrowAny { UUID.fromString(it.id) }
                        val fromDb = transaction { BookableEntity.findById(it.id) }
                        fromDb shouldNotBe null
                        fromDb!!
                        fromDb.id shouldBe it.id
                        fromDb.name shouldBe it.name
                        fromDb.posX shouldBe it.posX
                        fromDb.posY shouldBe it.posY
                        fromDb.index shouldBe it.index
                        fromDb.floorId.toString() shouldBe it.floorId
                        fromDb.typeId.toString() shouldBe it.typeId
                        fromDb.parentId.toString() shouldBe it.parentId
                    }
                }
            }
        }
    }
},)
