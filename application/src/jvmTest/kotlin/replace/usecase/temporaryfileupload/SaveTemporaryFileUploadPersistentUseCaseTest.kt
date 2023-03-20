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

class SaveTemporaryFileUploadPersistentUseCaseTest : FunSpec(
    {
        context("happy path") {
            test("simple case") {
                val fileStorage = InMemoryFileStorage()
                useDatabase {
                    checkAll(3, ReplaceArb.temporaryFile(fileStorage)) { tempFileUpload ->
                        newSuspendedTransaction {
                            TemporaryFile.findById(tempFileUpload.file.id) shouldNotBe null
                        }

                        fileStorage.exists(tempFileUpload.file.path) shouldBe true

                        val fromUseCase = SaveTemporaryFileUploadPersistentUseCase.execute(
                            tempFileUpload.file.id.value,
                            fileStorage,
                        )

                        fileStorage.exists(tempFileUpload.file.path) shouldBe false
                        fileStorage.exists(fromUseCase.path) shouldBe true

                        newSuspendedTransaction {
                            TemporaryFile.findById(tempFileUpload.file.id) shouldBe null
                        }

                        fromUseCase.name shouldBe tempFileUpload.file.name
                        fromUseCase.mime shouldBe tempFileUpload.file.mime
                        fromUseCase.extension shouldBe tempFileUpload.file.extension
                        fromUseCase.sizeInBytes shouldBe tempFileUpload.data.size
                    }
                }
            }
        }
    },
)
