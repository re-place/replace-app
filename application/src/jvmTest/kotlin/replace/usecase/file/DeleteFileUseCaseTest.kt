package replace.usecase.file

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.property.checkAll
import org.jetbrains.exposed.sql.transactions.transaction
import replace.datastore.InMemoryFileStorage
import replace.model.File
import replace.usecase.generator.ReplaceArb
import replace.usecase.generator.file
import replace.usecase.useDatabase

class DeleteFileUseCaseTest : FunSpec(
    {
        context("happy path") {
            test("delete a simple file") {
                useDatabase {
                    val fileStorage = InMemoryFileStorage()
                    checkAll(1, ReplaceArb.file(fileStorage)) { tempFile ->
                        fileStorage.exists(tempFile.file.path) shouldBe true
                        transaction {
                            File.findById(tempFile.file.id.value) shouldNotBe null
                        }
                        DeleteFileUseCase.execute(tempFile.file.id.value, fileStorage)

                        fileStorage.exists(tempFile.file.path) shouldBe false
                        transaction {
                            File.findById(tempFile.file.id.value) shouldBe null
                        }
                    }
                }
            }
        }
    },
)
