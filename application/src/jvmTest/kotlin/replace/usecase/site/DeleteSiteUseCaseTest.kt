package replace.usecase.site

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.property.checkAll
import org.jetbrains.exposed.sql.transactions.transaction
import replace.datastore.InMemoryFileStorage
import replace.model.Site
import replace.usecase.generator.ReplaceArb
import replace.usecase.generator.site
import replace.usecase.useDatabase

class DeleteSiteUseCaseTest : FunSpec(
    {
        context("happy path") {
            test("delete a simple site") {
                useDatabase {
                    val storage = InMemoryFileStorage()
                    checkAll(5, ReplaceArb.site()) { site ->

                        transaction {
                            Site.findById(site.id) shouldNotBe null
                        }
                        DeleteSiteUseCase.execute(site.id.toString(), storage)
                        transaction {
                            Site.findById(site.id) shouldBe null
                        }
                    }
                }
            }
        }
    },
)
