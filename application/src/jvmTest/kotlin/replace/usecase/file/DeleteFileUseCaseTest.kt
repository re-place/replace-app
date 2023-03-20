package replace.usecase.file

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll
import org.jetbrains.exposed.sql.transactions.transaction
import replace.datastore.InMemoryFileStorage
import replace.model.File
import replace.usecase.generator.ReplaceArb
import replace.usecase.generator.temporaryFile
import replace.usecase.useDatabase

class DeleteFileUseCaseTest : FunSpec(
    {
        context("happy path") {
            test("delete a simple file") {
                useDatabase {
                    val storage = InMemoryFileStorage()
                    checkAll(1, ReplaceArb.temporaryFile(storage)) { tempFile ->
                        DeleteFileUseCase.execute(tempFile.file.id.value, storage)

                        transaction {
                            File.findById(tempFile.file.id.value) shouldBe null
                        }
                    }
                }
            }
        }
    },
)
