package replace.usecase.site

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNot
import io.kotest.matchers.shouldNotBe
import io.kotest.property.checkAll
import replace.usecase.generator.ReplaceArb
import replace.usecase.generator.siteCreateDto
import replace.usecase.useDatabase
import java.util.UUID

class CreateSiteUseCaseTest : FunSpec(
    {
        context("happy path") {
            test("create a simple site") {
                useDatabase {
                    checkAll(ReplaceArb.siteCreateDto()) { dto ->
                        val fromUseCase = CreateSiteUseCase.execute(dto)

                        fromUseCase.id shouldNotBe null
                        shouldNotThrowAny { UUID.fromString(fromUseCase.id) }
                        fromUseCase.name shouldBe dto.name
                        fromUseCase.floors shouldBe null
                    }
                }
            }
        }
    }
)
