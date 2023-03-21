package replace.usecase.temporaryfileupload

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.property.checkAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import replace.datastore.InMemoryFileStorage
import replace.model.TemporaryFile
import replace.usecase.generator.ReplaceArb
import replace.usecase.generator.temporaryFile
import replace.usecase.useDatabase

class DeleteTemporaryFileUploadUseCaseTest : FunSpec(
    {
        context("happy path") {
            test("delete a simple temporary file upload by id") {
                val fileStorage = InMemoryFileStorage()
                useDatabase {
                    checkAll(5, ReplaceArb.temporaryFile(fileStorage)) { (_, file) ->
                        fileStorage.exists(file.path) shouldBe true
                        newSuspendedTransaction {
                            TemporaryFile.findById(file.id.value) shouldNotBe null
                        }

                        DeleteTemporaryFileUploadUseCase.execute(file.id.value, fileStorage)

                        fileStorage.exists(file.path) shouldBe false
                        newSuspendedTransaction {
                            TemporaryFile.findById(file.id.value) shouldBe null
                        }
                    }
                }
            }
            test("delete a simple temporary file upload by model") {
                val fileStorage = InMemoryFileStorage()
                useDatabase {
                    checkAll(5, ReplaceArb.temporaryFile(fileStorage)) { (_, file) ->
                        fileStorage.exists(file.path) shouldBe true
                        newSuspendedTransaction {
                            TemporaryFile.findById(file.id.value) shouldNotBe null
                        }

                        DeleteTemporaryFileUploadUseCase.execute(file, fileStorage)

                        fileStorage.exists(file.path) shouldBe false
                        newSuspendedTransaction {
                            TemporaryFile.findById(file.id.value) shouldBe null
                        }
                    }
                }
            }
        }
    },
)
