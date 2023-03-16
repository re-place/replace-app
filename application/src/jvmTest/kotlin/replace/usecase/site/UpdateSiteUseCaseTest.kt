package replace.usecase.site

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.property.arbitrary.next
import io.kotest.property.checkAll
import replace.dto.UpdateSiteDto
import replace.dto.toDto
import replace.model.Site
import replace.usecase.generator.ReplaceArb
import replace.usecase.generator.site
import replace.usecase.generator.siteDto
import replace.usecase.generator.siteUpdateDto
import replace.usecase.useDatabase
import java.util.*

class UpdateSiteUseCaseTest : FunSpec(
    {
        context("happy path") {
            test("update a simple site") {
                useDatabase {
                    checkAll(ReplaceArb.site()) { site ->
                        val dtoPreUpdate = site.toDto()
                        val updateDto: UpdateSiteDto = ReplaceArb.siteUpdateDto(dtoPreUpdate).next()

                        val fromUseCase = UpdateSiteUseCase.execute(updateDto)

                        fromUseCase.id shouldNotBe null
                        shouldNotThrowAny { UUID.fromString(fromUseCase.id) }
                        fromUseCase.id shouldBe dtoPreUpdate.id

                        fromUseCase.name shouldNotBe null
                        fromUseCase.name shouldNotBe dtoPreUpdate.name
                        fromUseCase.name shouldBe updateDto.name
                    }
                }
            }
        }
    }
)
