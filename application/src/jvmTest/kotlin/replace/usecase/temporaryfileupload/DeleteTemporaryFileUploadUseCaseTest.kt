package replace.usecase.temporaryfileupload

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll
import replace.datastore.InMemoryFileStorage
import replace.usecase.generator.ReplaceArb
import replace.usecase.generator.temporaryFile
import replace.usecase.useDatabase

class DeleteTemporaryFileUploadUseCaseTest : FunSpec(
    {
        context("happy path") {
            test("delete a simple temporary file upload") {
                val fileStorage = InMemoryFileStorage()
                useDatabase {
                    checkAll(10, ReplaceArb.temporaryFile(fileStorage)) { (data, file) ->
                        fileStorage.exists(file.path) shouldBe true
                        DeleteTemporaryFileUploadUseCase.execute(file, fileStorage)
                        fileStorage.exists(file.path) shouldBe false
                    }
                }
            }
        }
    },
)
